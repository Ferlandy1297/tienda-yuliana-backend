import 'api_client.dart';

class DevolucionService {
  final ApiClient _client;
  DevolucionService({required String token}) : _client = ApiClient(token: token);

  Future<void> crear({required int idProveedor, required List<Map<String, dynamic>> items, String? motivo}) async {
    await _client.dio.post('/api/devoluciones-proveedor', data: {
      'idProveedor': idProveedor,
      if (motivo != null) 'motivo': motivo,
      'items': items,
    });
  }
}

