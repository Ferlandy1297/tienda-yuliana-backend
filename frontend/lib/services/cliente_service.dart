import '../models/cliente.dart';
import 'api_client.dart';

class ClienteService {
  final ApiClient _client;
  ClienteService({required String token}) : _client = ApiClient(token: token);

  Future<List<Cliente>> list() async {
    final res = await _client.dio.get('/api/clientes');
    final data = (res.data as List).cast<Map<String, dynamic>>();
    return data.map(Cliente.fromJson).toList();
  }

  Future<Cliente> create(Cliente c) async {
    final res = await _client.dio.post('/api/clientes', data: c.toRequest());
    return Cliente.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Cliente> update(Cliente c) async {
    final res = await _client.dio.put('/api/clientes/${c.idCliente}', data: c.toRequest());
    return Cliente.fromJson(res.data as Map<String, dynamic>);
  }
}

