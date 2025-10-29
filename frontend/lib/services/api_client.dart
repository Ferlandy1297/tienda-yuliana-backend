import 'package:dio/dio.dart';
import '../config.dart';

class ApiClient {
  final Dio _dio;

  ApiClient._(this._dio);

  factory ApiClient({String? token}) {
    final dio = Dio(
      BaseOptions(
        baseUrl: AppConfig.apiBaseUrl,
        connectTimeout: const Duration(seconds: 10),
        receiveTimeout: const Duration(seconds: 20),
        headers: {
          if (token != null) 'Authorization': 'Basic $token',
          'Content-Type': 'application/json',
        },
      ),
    );
    return ApiClient._(dio);
  }

  Dio get dio => _dio;
}
