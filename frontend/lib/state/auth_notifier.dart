import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/auth_models.dart';
import '../services/auth_service.dart';

class AuthState {
  final bool loading;
  final String? token;
  final String? role; // 'admin' | 'user'
  final String? error;

  const AuthState({this.loading = false, this.token, this.role, this.error});

  bool get isAuthenticated => token != null && token!.isNotEmpty;

  AuthState copyWith({bool? loading, String? token, String? role, String? error}) {
    return AuthState(
      loading: loading ?? this.loading,
      token: token ?? this.token,
      role: role ?? this.role,
      error: error,
    );
  }
}

class AuthNotifier extends StateNotifier<AuthState> {
  final AuthService _service;
  AuthNotifier(this._service) : super(const AuthState());

  static const _kToken = 'auth_token';
  static const _kRole = 'auth_role';

  Future<void> loadFromStorage() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString(_kToken);
    final role = prefs.getString(_kRole);
    if (token != null && role != null) {
      state = state.copyWith(token: token, role: role);
    }
  }

  Future<void> login(String username, String password) async {
    state = state.copyWith(loading: true, error: null);
    try {
      final res = await _service.login(LoginRequest(username: username, password: password));
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_kToken, res.token);
      await prefs.setString(_kRole, res.role);
      state = state.copyWith(loading: false, token: res.token, role: res.role);
    } catch (e) {
      state = state.copyWith(loading: false, error: e.toString());
    }
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_kToken);
    await prefs.remove(_kRole);
    state = const AuthState();
  }
}

final authServiceProvider = Provider<AuthService>((ref) => AuthService());
final authProvider = StateNotifierProvider<AuthNotifier, AuthState>((ref) {
  return AuthNotifier(ref.read(authServiceProvider));
});

