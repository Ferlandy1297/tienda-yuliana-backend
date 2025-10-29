import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
// ignore: avoid_web_libraries_in_flutter
import 'dart:html' as html;
import '../../state/auth_notifier.dart';
import '../../services/reporte_service.dart';
import '../../widgets/responsive_scaffold.dart';

class ReportsPage extends ConsumerStatefulWidget {
  const ReportsPage({super.key});
  @override
  ConsumerState<ReportsPage> createState() => _ReportsPageState();
}

class _ReportsPageState extends ConsumerState<ReportsPage> {
  String _periodo = 'diario';
  String _inicio = '';
  String _fin = '';
  Map<String, dynamic>? _ventas;
  Map<String, dynamic>? _util;
  Map<String, dynamic>? _serie;
  Map<String, dynamic>? _top;
  int _topN = 10;
  bool _loading = false;
  String? _error;

  ReporteService _svc() => ReporteService(token: ref.read(authProvider).token!);

  void _snack(String msg, {bool error = false}) {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg), backgroundColor: error ? Colors.red : null));
  }

  Future<void> _cargarVentas() async {
    setState(() { _loading = true; _error = null; });
    try {
      if (_inicio.isNotEmpty && _fin.isNotEmpty) {
        _ventas = await _svc().ventas(inicio: _inicio, fin: _fin);
      } else {
        _ventas = await _svc().ventas(periodo: _periodo);
      }
    } catch (e) { _error = e.toString(); } finally { if (mounted) setState(() => _loading = false); }
  }

  Future<void> _cargarUtilidades() async {
    if (_inicio.isEmpty || _fin.isEmpty) { _snack('Defina inicio y fin'); return; }
    setState(() { _loading = true; _error = null; });
    try { _util = await _svc().utilidades(inicio: _inicio, fin: _fin); } catch (e) { _error = e.toString(); } finally { if (mounted) setState(()=>_loading=false);}  
  }

  Future<void> _cargarSerie() async {
    if (_inicio.isEmpty || _fin.isEmpty) { _snack('Defina inicio y fin'); return; }
    setState(() { _loading = true; _error = null; });
    try { _serie = await _svc().serie(inicio: _inicio, fin: _fin); } catch (e) { _error = e.toString(); } finally { if (mounted) setState(()=>_loading=false);}  
  }

  Future<void> _cargarTop() async {
    if (_inicio.isEmpty || _fin.isEmpty) { _snack('Defina inicio y fin'); return; }
    setState(() { _loading = true; _error = null; });
    try { _top = await _svc().masVendidos(inicio: _inicio, fin: _fin, top: _topN); } catch (e) { _error = e.toString(); } finally { if (mounted) setState(()=>_loading=false);}  
  }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Reportes (ADMIN)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Wrap(spacing: 12, runSpacing: 8, crossAxisAlignment: WrapCrossAlignment.center, children: [
            DropdownButton<String>(
              value: _periodo,
              items: const [
                DropdownMenuItem(value: 'diario', child: Text('Diario')),
                DropdownMenuItem(value: 'quincenal', child: Text('Quincenal')),
                DropdownMenuItem(value: 'mensual', child: Text('Mensual')),
              ],
              onChanged: (v) => setState(() => _periodo = v ?? 'diario'),
            ),
            SizedBox(width: 220, child: TextField(decoration: const InputDecoration(labelText: 'Inicio (ISO8601)'), onChanged: (v)=>_inicio=v)),
            SizedBox(width: 220, child: TextField(decoration: const InputDecoration(labelText: 'Fin (ISO8601)'), onChanged: (v)=>_fin=v)),
            FilledButton(onPressed: _loading ? null : _cargarVentas, child: const Text('Cargar Ventas')),
            OutlinedButton(
              onPressed: () {
                final url = _svc().ventasCsvUrl(periodo: _periodo);
                html.window.open(url, '_blank');
              },
              child: const Text('Descargar Ventas CSV'),
            ),
          ]),
          const SizedBox(height: 12),
          if (_error != null) Text(_error!, style: const TextStyle(color: Colors.red)),
          if (_ventas != null)
            Card(child: ListTile(title: const Text('Resumen Ventas'), subtitle: Text('Cantidad: ${_ventas!['cantidad']} | Total: ${_ventas!['total']}'))),
          const SizedBox(height: 12),
          Wrap(spacing: 12, runSpacing: 8, crossAxisAlignment: WrapCrossAlignment.center, children: [
            SizedBox(width: 220, child: TextField(decoration: const InputDecoration(labelText: 'Inicio (ISO8601)'), onChanged: (v)=>_inicio=v)),
            SizedBox(width: 220, child: TextField(decoration: const InputDecoration(labelText: 'Fin (ISO8601)'), onChanged: (v)=>_fin=v)),
            FilledButton(onPressed: _loading ? null : _cargarUtilidades, child: const Text('Calcular Utilidades')),
            FilledButton(onPressed: _loading ? null : _cargarSerie, child: const Text('Serie Ventas')),
            Row(mainAxisSize: MainAxisSize.min, children: [
              const Text('Top N:'), const SizedBox(width: 8), SizedBox(width: 70, child: TextField(keyboardType: TextInputType.number, controller: TextEditingController(text: '10'), onChanged: (v){ final n=int.tryParse(v); if(n!=null) _topN=n; })),
              const SizedBox(width: 8), FilledButton(onPressed: _loading ? null : _cargarTop, child: const Text('Más vendidos')),
            ]),
          ]),
          const SizedBox(height: 12),
          if (_util != null)
            Card(child: ListTile(title: const Text('Utilidades'), subtitle: Text('Ventas: ${_util!['ventas']} | Costo: ${_util!['costo']} | Utilidad: ${_util!['utilidad']}'))),
          if (_serie != null)
            Expanded(child: Card(child: Padding(padding: const EdgeInsets.all(12), child: _SerieTable(data: (_serie!['items'] as List).cast())))),
          if (_top != null)
            Expanded(child: Card(child: Padding(padding: const EdgeInsets.all(12), child: _TopTable(data: (_top!['items'] as List).cast())))),
        ]),
      ),
    );
  }
}

class _SerieTable extends StatelessWidget {
  final List data;
  const _SerieTable({required this.data});
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: DataTable(columns: const [
        DataColumn(label: Text('Fecha')),
        DataColumn(label: Text('Total')),
      ], rows: data.map((e) => DataRow(cells: [
        DataCell(Text(e['fecha']?.toString() ?? '-')),
        DataCell(Text(e['total']?.toString() ?? '0')),
      ])).toList()),
    );
  }
}

class _TopTable extends StatelessWidget {
  final List data;
  const _TopTable({required this.data});
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: DataTable(columns: const [
        DataColumn(label: Text('Producto')),
        DataColumn(label: Text('Cantidad')),
      ], rows: data.map((e) => DataRow(cells: [
        DataCell(Text(e['nombre']?.toString() ?? '-')),
        DataCell(Text(e['cantidad']?.toString() ?? '0')),
      ])).toList()),
    );
  }
}

