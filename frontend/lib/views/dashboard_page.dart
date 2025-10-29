import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../state/auth_notifier.dart';
import 'package:go_router/go_router.dart';
import '../widgets/responsive_scaffold.dart';

class DashboardPage extends ConsumerWidget {
  const DashboardPage({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final auth = ref.watch(authProvider);
    return ResponsiveScaffold(
      title: 'Panel',
      actions: [
        TextButton.icon(
          onPressed: () => ref.read(authProvider.notifier).logout().then((_) => context.go('/login')),
          icon: const Icon(Icons.logout),
          label: const Text('Salir'),
        )
      ],
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: GridView.count(
          crossAxisCount: MediaQuery.of(context).size.width > 900 ? 3 : 1,
          mainAxisSpacing: 16,
          crossAxisSpacing: 16,
          children: [
            _StatCard(title: 'Bienvenido', value: auth.role ?? 'sin rol', icon: Icons.person),
            _NavCard(title: 'Productos', subtitle: 'Alertas y creación', icon: Icons.shopping_cart, route: '/products'),
            _NavCard(title: 'Proveedores', subtitle: 'Listado y edición', icon: Icons.store, route: '/proveedores'),
            _NavCard(title: 'Clientes', subtitle: 'Listado y edición', icon: Icons.person, route: '/clientes'),
            if (auth.role == 'admin') _NavCard(title: 'Reportes', subtitle: 'Ventas, utilidades, top', icon: Icons.analytics, route: '/reportes'),
            if (auth.role == 'admin') _NavCard(title: 'Lotes', subtitle: 'Por vencer', icon: Icons.inventory, route: '/lotes'),
            if (auth.role == 'admin' || auth.role == 'empleado') _NavCard(title: 'Fiados', subtitle: 'Saldo y abonos', icon: Icons.account_balance_wallet, route: '/fiados'),
            if (auth.role == 'admin') _NavCard(title: 'Mermas', subtitle: 'Registrar', icon: Icons.remove_circle_outline, route: '/mermas'),
            if (auth.role == 'admin') _NavCard(title: 'Devoluciones', subtitle: 'a Proveedor', icon: Icons.assignment_return, route: '/devoluciones'),
          ],
        ),
      ),
    );
  }
}

class _StatCard extends StatelessWidget {
  final String title;
  final String value;
  final IconData icon;
  const _StatCard({required this.title, required this.value, required this.icon});
  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          children: [
            Icon(icon, size: 36),
            const SizedBox(width: 16),
            Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              Text(title, style: const TextStyle(fontSize: 16, color: Colors.grey)),
              Text(value, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w600)),
            ])
          ],
        ),
      ),
    );
  }
}

class _NavCard extends StatelessWidget {
  final String title;
  final String subtitle;
  final IconData icon;
  final String route;
  const _NavCard({required this.title, required this.subtitle, required this.icon, required this.route});
  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () => context.go(route),
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(children: [
            Icon(icon, size: 36),
            const SizedBox(width: 16),
            Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              Text(title, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w600)),
              Text(subtitle, style: const TextStyle(color: Colors.grey)),
            ])),
            const Icon(Icons.chevron_right)
          ]),
        ),
      ),
    );
  }
}

