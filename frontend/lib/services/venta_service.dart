import 'api_client.dart';

class VentaService {
  final ApiClient _client;
  VentaService({required String token}) : _client = ApiClient(token: token);

  Future<Map<String, dynamic>> crear(Map<String, dynamic> body) async {
    final res = await _client.dio.post('/api/ventas', data: body);
    return (res.data as Map).cast<String, dynamic>();
  }
}

