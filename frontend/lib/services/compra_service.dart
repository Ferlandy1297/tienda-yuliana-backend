import 'api_client.dart';

class CompraService {
  final ApiClient _client;
  CompraService({required String token}) : _client = ApiClient(token: token);

  Future<Map<String, dynamic>> reporte(String periodo) async {
    final res = await _client.dio.get('/api/compras/reporte', queryParameters: {'periodo': periodo});
    return (res.data as Map).cast<String, dynamic>();
  }

  String reporteCsvUrl(String periodo) => '${_client.dio.options.baseUrl}/api/compras/reporte.csv?periodo=$periodo';

  Future<void> crear(Map<String, dynamic> body) async {
    await _client.dio.post('/api/compras', data: body);
  }

  Future<void> pagar(int idCompra, double monto, {String metodo = 'EFECTIVO', String? observacion}) async {
    await _client.dio.post('/api/compras/pago', data: {
      'idCompra': idCompra,
      'metodo': metodo,
      'monto': monto,
      if (observacion != null) 'observacion': observacion,
    });
  }
}

