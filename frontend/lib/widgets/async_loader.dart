import 'package:flutter/material.dart';

class AsyncLoader extends StatelessWidget {
  final bool loading;
  final String? error;
  final Widget child;

  const AsyncLoader({super.key, required this.loading, required this.child, this.error});

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (error != null) {
      return Center(child: Text(error!, style: const TextStyle(color: Colors.red)));
    }
    return child;
  }
}

