class LoginRequest {
  final String username;
  final String password;
  LoginRequest({required this.username, required this.password});

  Map<String, dynamic> toJson() => {
        'username': username,
        'password': password,
      };
}

class LoginResponse {
  final String token;
  final String role; // 'admin' | 'user'
  LoginResponse({required this.token, required this.role});

  factory LoginResponse.fromJson(Map<String, dynamic> json) => LoginResponse(
        token: json['token'] as String,
        role: json['role'] as String,
      );
}

