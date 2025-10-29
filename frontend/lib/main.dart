import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'app_router.dart';
import 'state/auth_notifier.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final container = ProviderContainer();
  final auth = container.read(authProvider.notifier);
  await auth.loadFromStorage();
  runApp(UncontrolledProviderScope(container: container, child: const App()));
}

class App extends ConsumerWidget {
  const App({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = AppRouter.create(ref);
    return MaterialApp.router(
      title: 'App Roles',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(useMaterial3: true, colorSchemeSeed: Colors.indigo),
      routerConfig: router,
    );
  }
}

// GoRouter is created in AppRouter; Riverpod manages auth state
