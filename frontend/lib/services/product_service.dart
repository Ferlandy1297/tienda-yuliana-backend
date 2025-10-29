import 'package:dio/dio.dart';
import '../models/product.dart';
import 'api_client.dart';

class ProductService {
  final ApiClient _client;
  ProductService({required String token}) : _client = ApiClient(token: token);

  Future<List<Product>> alertasStockBajo() async {
    final res = await _client.dio.get('/api/productos/alertas/stock-bajo');
    final data = (res.data as List).cast<Map<String, dynamic>>();
    return data.map(Product.fromJson).toList();
  }

  Future<Product> create(Product p) async {
    final res = await _client.dio.post('/api/productos', data: p.toCreateJson());
    return Product.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Product> updatePrecio(int idProducto, double nuevoPrecio) async {
    final res = await _client.dio.put('/api/productos/$idProducto/precio', data: {'precioVenta': nuevoPrecio});
    return Product.fromJson(res.data as Map<String, dynamic>);
  }

  Future<Map<String, dynamic>> notificarStockBajo({String? emailTo}) async {
    final res = await _client.dio.post('/api/productos/alertas/stock-bajo/notificar');
    return (res.data as Map).cast<String, dynamic>();
  }

  // No hay DELETE de productos expuesto en backend actual
}
