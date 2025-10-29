import 'api_client.dart';

class ReporteService {
  final ApiClient _client;
  ReporteService({required String token}) : _client = ApiClient(token: token);

  Future<Map<String, dynamic>> ventas({String? periodo, String? inicio, String? fin}) async {
    final params = <String, dynamic>{};
    if (periodo != null) params['periodo'] = periodo;
    if (inicio != null) params['inicio'] = inicio;
    if (fin != null) params['fin'] = fin;
    final res = await _client.dio.get('/api/reportes/ventas', queryParameters: params);
    return (res.data as Map).cast<String, dynamic>();
  }

  String ventasCsvUrl({required String periodo}) => '${_client.dio.options.baseUrl}/api/reportes/ventas.csv?periodo=$periodo';

  Future<Map<String, dynamic>> masVendidos({required String inicio, required String fin, int top = 10}) async {
    final res = await _client.dio.get('/api/reportes/mas-vendidos', queryParameters: {'inicio': inicio, 'fin': fin, 'top': top});
    return (res.data as Map).cast<String, dynamic>();
  }

  Future<Map<String, dynamic>> utilidades({required String inicio, required String fin}) async {
    final res = await _client.dio.get('/api/reportes/utilidades', queryParameters: {'inicio': inicio, 'fin': fin});
    return (res.data as Map).cast<String, dynamic>();
  }

  Future<Map<String, dynamic>> serie({required String inicio, required String fin, String granularidad = 'day'}) async {
    final res = await _client.dio.get('/api/reportes/ventas/serie', queryParameters: {'inicio': inicio, 'fin': fin, 'granularidad': granularidad});
    return (res.data as Map).cast<String, dynamic>();
  }
}

