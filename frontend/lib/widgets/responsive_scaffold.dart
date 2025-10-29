import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class ResponsiveScaffold extends StatelessWidget {
  final String title;
  final Widget body;
  final List<Widget>? actions;
  final Widget? floatingActionButton;

  const ResponsiveScaffold({super.key, required this.title, required this.body, this.actions, this.floatingActionButton});

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final isWide = constraints.maxWidth >= 900;
        final content = Row(
          children: [
            if (isWide) _SideNav(),
            Expanded(child: body),
          ],
        );
        return Scaffold(
          appBar: AppBar(title: Text(title), actions: actions),
          drawer: isWide ? null : Drawer(child: _SideNav()),
          body: SafeArea(child: content),
          floatingActionButton: floatingActionButton,
        );
      },
    );
  }
}

class _SideNav extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final rip = GoRouter.of(context).routeInformationProvider.value;
    final current = (rip.uri?.toString()) ?? (rip.location ?? '');
    return NavigationRail(
      selectedIndex: _indexFor(current),
      onDestinationSelected: (i) {
        final dest = ['/', '/products', '/compras', '/proveedores', '/clientes'][i];
        context.go(dest);
      },
      labelType: NavigationRailLabelType.all,
      destinations: const [
        NavigationRailDestination(icon: Icon(Icons.dashboard_outlined), selectedIcon: Icon(Icons.dashboard), label: Text('Inicio')),
        NavigationRailDestination(icon: Icon(Icons.shopping_cart_outlined), selectedIcon: Icon(Icons.shopping_cart), label: Text('Productos')),
        NavigationRailDestination(icon: Icon(Icons.receipt_long_outlined), selectedIcon: Icon(Icons.receipt_long), label: Text('Compras')),
        NavigationRailDestination(icon: Icon(Icons.store_outlined), selectedIcon: Icon(Icons.store), label: Text('Proveedores')),
        NavigationRailDestination(icon: Icon(Icons.person_outline), selectedIcon: Icon(Icons.person), label: Text('Clientes')),
      ],
    );
  }

  int _indexFor(String route) {
    if (route.contains('products')) return 1;
    if (route.contains('compras')) return 2;
    if (route.contains('proveedores')) return 3;
    if (route.contains('clientes')) return 4;
    return 0;
  }
}
