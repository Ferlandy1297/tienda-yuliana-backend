import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../services/lote_service.dart';
import '../../widgets/responsive_scaffold.dart';

class LotesPage extends ConsumerStatefulWidget {
  const LotesPage({super.key});
  @override
  ConsumerState<LotesPage> createState() => _LotesPageState();
}

class _LotesPageState extends ConsumerState<LotesPage> {
  int _dias = 30;
  bool _loading = false;
  String? _error;
  List<Map<String, dynamic>> _items = [];

  LoteService _svc() => LoteService(token: ref.read(authProvider).token!);

  Future<void> _load() async {
    setState(() { _loading = true; _error = null; });
    try { _items = await _svc().porVencer(_dias); } catch (e) { _error = e.toString(); } finally { if (mounted) setState(()=>_loading=false);}  
  }

  @override
  void initState() { super.initState(); _load(); }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Lotes por vencer (ADMIN)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(children: [
          Row(children: [
            const Text('Días:'), const SizedBox(width: 8),
            SizedBox(width: 80, child: TextField(keyboardType: TextInputType.number, controller: TextEditingController(text: _dias.toString()), onChanged: (v){ final d=int.tryParse(v); if(d!=null) _dias=d; })),
            const SizedBox(width: 8),
            FilledButton(onPressed: _loading ? null : _load, child: const Text('Actualizar')),
          ]),
          const SizedBox(height: 12),
          if (_error != null) Text(_error!, style: const TextStyle(color: Colors.red)),
          Expanded(child: Card(child: SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: DataTable(columns: const [
              DataColumn(label: Text('Producto')),
              DataColumn(label: Text('Lote')),
              DataColumn(label: Text('Vence')),
              DataColumn(label: Text('Cant. Disponible')),
            ], rows: _items.map((e) => DataRow(cells: [
              DataCell(Text(e['producto']?['nombre']?.toString() ?? '-')),
              DataCell(Text(e['idLote']?.toString() ?? '-')),
              DataCell(Text(e['fechaVencimiento']?.toString() ?? '-')),
              DataCell(Text(e['cantidadDisponible']?.toString() ?? '-')),
            ])).toList()),
          ))),
        ]),
      ),
    );
  }
}

