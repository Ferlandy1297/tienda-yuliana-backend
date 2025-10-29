import 'dart:convert';
import 'package:dio/dio.dart';
import '../models/auth_models.dart';
import 'api_client.dart';

class AuthService {
  final ApiClient _client;
  AuthService({String? token}) : _client = ApiClient(token: token);

  Future<LoginResponse> login(LoginRequest request) async {
    final basic = base64Encode(utf8.encode('${request.username}:${request.password}'));
    final client = ApiClient(token: basic);
    try {
      final res = await client.dio.get('/api/auth/check');
      final data = res.data as Map<String, dynamic>;
      final authorities = (data['authorities'] as List)
          .map((e) => (e is Map && e.containsKey('authority')) ? e['authority'].toString() : e.toString())
          .toList();
      String role = 'user';
      if (authorities.any((a) => a.contains('ROLE_ADMIN'))) role = 'admin';
      else if (authorities.any((a) => a.contains('ROLE_EMPLEADO'))) role = 'empleado';
      return LoginResponse(token: basic, role: role);
    } on DioException catch (e) {
      final status = e.response?.statusCode;
      if (status == 401) throw Exception('Credenciales inválidas');
      throw Exception('Error de autenticación');
    }
  }
}
