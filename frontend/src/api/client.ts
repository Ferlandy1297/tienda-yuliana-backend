export class ApiError extends Error {
  status: number
  constructor(status: number, message: string) {
    super(message)
    this.status = status
  }
}

const BASE_URL = (import.meta.env.VITE_API_URL ?? '').replace(/\/$/, '')
const SESSION_KEY = 'app:session'

function logoutAndRedirect() {
  try {
    localStorage.removeItem(SESSION_KEY)
  } catch {
    /* ignore */
  }
  window.location.href = '/login'
}

type FetchOptions = Omit<RequestInit, 'body' | 'headers' | 'credentials'> & {
  body?: unknown
  headers?: Record<string, string>
}

/**
 * JSON fetch wrapper:
 * - Base URL desde VITE_API_URL
 * - Content-Type: application/json
 * - Sin Authorization, sin cookies (credentials: 'omit')
 * - 401/403 => limpia sesión y redirige a /login
 * - Lanza ApiError con status y message
 */
export async function fetchJson<T = unknown>(
  path: string,
  options: FetchOptions = {}
): Promise<T> {
  const url = path.startsWith('http')
    ? path
    : `${BASE_URL}/${path.replace(/^\//, '')}`

  const { body, headers, ...rest } = options

  const init: RequestInit = {
    ...rest,
    headers: {
      'Content-Type': 'application/json',
      ...(headers ?? {}),
    },
    body: body !== undefined ? JSON.stringify(body) : undefined,
    credentials: 'omit',
  }

  const res = await fetch(url, init)

  // 401/403 centralizado
  if (res.status === 401 || res.status === 403) {
    logoutAndRedirect()
    throw new ApiError(res.status, 'Unauthorized')
  }

  // Si no es ok, intenta extraer mensaje de error
  if (!res.ok) {
    let message = `HTTP ${res.status} ${(res.statusText || '').trim()}`
    try {
      const ct = res.headers.get('content-type') || ''
      if (ct.includes('application/json')) {
        const data = await res.json()
        message =
          ((data as any)?.message || (data as any)?.error || (data as any)?.detail) || message
      } else {
        const text = await res.text()
        if (text) message = text
      }
    } catch {
      // ignore parse errors
    }
    throw new ApiError(res.status, message)
  }

  // Éxito: parsea JSON si hay
  const ct = res.headers.get('content-type') || ''
  if (ct.includes('application/json')) {
    return (await res.json()) as T
  }
  // Respuestas vacías o texto
  const text = await res.text()
  return (text ? ((text as unknown) as T) : ((null as unknown) as T))
}

/** Construye query string con ? al inicio cuando haya parámetros */
export function toQuery(
  params?: Record<string, string | number | boolean | null | undefined> | URLSearchParams | null
): string {
  if (!params) return ''
  if (params instanceof URLSearchParams) {
    const s = params.toString()
    return s ? `?${s}` : ''
  }
  const usp = new URLSearchParams()
  Object.entries(params).forEach(([k, v]) => {
    if (v === undefined || v === null || v === '') return
    usp.set(k, String(v))
  })
  const s = usp.toString()
  return s ? `?${s}` : ''
}
