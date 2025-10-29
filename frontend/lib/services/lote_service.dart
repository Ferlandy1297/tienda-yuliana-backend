import 'api_client.dart';

class LoteService {
  final ApiClient _client;
  LoteService({required String token}) : _client = ApiClient(token: token);

  Future<List<Map<String, dynamic>>> porVencer(int dias) async {
    final res = await _client.dio.get('/api/lotes/por-vencer', queryParameters: {'dias': dias});
    final list = (res.data as List).cast<Map>();
    return list.map((e) => e.cast<String, dynamic>()).toList();
  }
}

