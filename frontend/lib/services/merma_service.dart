import 'api_client.dart';

class MermaService {
  final ApiClient _client;
  MermaService({required String token}) : _client = ApiClient(token: token);

  Future<void> registrar({required int idProducto, int? idLote, required int cantidad, required String motivo, String? observacion}) async {
    await _client.dio.post('/api/mermas', data: {
      'idProducto': idProducto,
      if (idLote != null) 'idLote': idLote,
      'cantidad': cantidad,
      'motivo': motivo,
      if (observacion != null) 'observacion': observacion,
    });
  }
}

