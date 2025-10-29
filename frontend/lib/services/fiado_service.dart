import 'api_client.dart';

class FiadoService {
  final ApiClient _client;
  FiadoService({required String token}) : _client = ApiClient(token: token);

  Future<Map<String, dynamic>> saldo(int idCliente) async {
    final res = await _client.dio.get('/api/fiados/saldo/$idCliente');
    return (res.data as Map).cast<String, dynamic>();
  }

  Future<Map<String, dynamic>> abonar({required int idCliente, required double monto}) async {
    final res = await _client.dio.post('/api/fiados/abonos', data: {'idCliente': idCliente, 'monto': monto});
    return (res.data as Map).cast<String, dynamic>();
  }
}

