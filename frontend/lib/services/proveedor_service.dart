import '../models/proveedor.dart';
import 'api_client.dart';

class ProveedorService {
  final ApiClient _client;
  ProveedorService({required String token}) : _client = ApiClient(token: token);

  Future<List<Proveedor>> list() async {
    final res = await _client.dio.get('/api/proveedores');
    final data = (res.data as List).cast<Map<String, dynamic>>();
    return data.map(Proveedor.fromJson).toList();
  }

  Future<Proveedor> create(Proveedor p) async {
    final res = await _client.dio.post('/api/proveedores', data: p.toRequest());
    return Proveedor.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Proveedor> update(Proveedor p) async {
    final res = await _client.dio.put('/api/proveedores/${p.id}', data: p.toRequest());
    return Proveedor.fromJson(res.data as Map<String, dynamic>);
  }
}

