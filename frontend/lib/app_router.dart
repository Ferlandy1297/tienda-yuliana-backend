import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'state/auth_notifier.dart';
import 'views/login_page.dart';
import 'views/dashboard_page.dart';
import 'views/products/products_page.dart';
import 'views/providers/providers_page.dart';
import 'views/clients/clients_page.dart';
import 'views/reports/reports_page.dart';
import 'views/lotes/lotes_page.dart';
import 'views/fiados/fiados_page.dart';
import 'views/mermas/merma_page.dart';
import 'views/devoluciones/devolucion_page.dart';
import 'views/ventas/venta_page.dart';
import 'views/compras/compras_page.dart';

class AppRouter {
  static GoRouter create(WidgetRef ref) {
    return GoRouter(
      initialLocation: '/login',
      redirect: (context, state) {
        final auth = ref.read(authProvider);
        final loggingIn = state.matchedLocation == '/login';
        if (!auth.isAuthenticated) {
          return loggingIn ? null : '/login';
        }
        if (loggingIn && auth.isAuthenticated) return '/';
        return null;
      },
      routes: [
        GoRoute(path: '/login', builder: (c, s) => const LoginPage()),
        GoRoute(
          path: '/',
          builder: (c, s) => const DashboardPage(),
          routes: [
            GoRoute(path: 'products', builder: (c, s) => const ProductsPage()),
            GoRoute(
              path: 'compras',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin') return const _ForbiddenPage();
                return const ComprasPage();
              },
            ),
            GoRoute(
              path: 'proveedores',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin' && role != 'empleado') {
                  return const _ForbiddenPage();
                }
                return const ProvidersPage();
              },
            ),
            GoRoute(
              path: 'clientes',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin' && role != 'empleado') {
                  return const _ForbiddenPage();
                }
                return const ClientsPage();
              },
            ),
            GoRoute(
              path: 'clientes',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin' && role != 'empleado') {
                  return const _ForbiddenPage();
                }
                return const ClientsPage();
              },
            ),
            GoRoute(
              path: 'reportes',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin') return const _ForbiddenPage();
                return const ReportsPage();
              },
            ),
            GoRoute(
              path: 'lotes',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin') return const _ForbiddenPage();
                return const LotesPage();
              },
            ),
            GoRoute(
              path: 'fiados',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin' && role != 'empleado') return const _ForbiddenPage();
                return const FiadosPage();
              },
            ),
            GoRoute(
              path: 'mermas',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin') return const _ForbiddenPage();
                return const MermaPage();
              },
            ),
            GoRoute(
              path: 'devoluciones',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin') return const _ForbiddenPage();
                return const DevolucionPage();
              },
            ),
            GoRoute(
              path: 'ventas',
              builder: (c, s) {
                final role = ref.read(authProvider).role;
                if (role != 'admin' && role != 'empleado') return const _ForbiddenPage();
                return const VentaPage();
              },
            ),
          ],
        ),
      ],
    );
  }
}

class _ForbiddenPage extends StatelessWidget {
  const _ForbiddenPage();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Acceso denegado')),
      body: const Center(child: Text('No tienes permisos para ver esta página.')),
    );
  }
}
