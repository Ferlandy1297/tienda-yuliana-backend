import 'dart:html' as html;
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../widgets/responsive_scaffold.dart';
import '../../services/compra_service.dart';

class ComprasPage extends ConsumerStatefulWidget {
  const ComprasPage({super.key});
  @override
  ConsumerState<ComprasPage> createState() => _ComprasPageState();
}

class _ComprasPageState extends ConsumerState<ComprasPage> {
  bool _loading = false;
  String? _error;

  // Crear compra
  final _idProveedor = TextEditingController();
  String _condicion = 'CONTADO';
  final _observacion = TextEditingController();
  final List<_CompraItem> _items = [
    _CompraItem(),
  ];

  // Pagar compra
  final _idCompra = TextEditingController();
  final _montoPago = TextEditingController();
  String _metodo = 'EFECTIVO';
  final _obsPago = TextEditingController();

  // Reporte compras
  String _periodo = 'diario';
  Map<String, dynamic>? _reporte;

  CompraService _svc() => CompraService(token: ref.read(authProvider).token!);

  void _snack(String msg, {bool error = false}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(msg), backgroundColor: error ? Colors.red : null),
    );
  }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Compras (ADMIN)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Crear compra', style: Theme.of(context).textTheme.titleMedium),
              const SizedBox(height: 8),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Wrap(spacing: 12, runSpacing: 8, crossAxisAlignment: WrapCrossAlignment.center, children: [
                        SizedBox(width: 200, child: TextField(controller: _idProveedor, decoration: const InputDecoration(labelText: 'ID Proveedor'), keyboardType: TextInputType.number)),
                        DropdownButton<String>(
                          value: _condicion,
                          items: const [
                            DropdownMenuItem(value: 'CONTADO', child: Text('CONTADO')),
                            DropdownMenuItem(value: 'CREDITO', child: Text('CREDITO')),
                          ],
                          onChanged: (v) => setState(() => _condicion = v ?? 'CONTADO'),
                        ),
                        SizedBox(width: 260, child: TextField(controller: _observacion, decoration: const InputDecoration(labelText: 'Observación'))),
                        FilledButton.icon(
                          onPressed: () => setState(() => _items.add(_CompraItem())),
                          icon: const Icon(Icons.add),
                          label: const Text('Agregar ítem'),
                        ),
                      ]),
                      const SizedBox(height: 8),
                      SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: DataTable(
                          columns: const [
                            DataColumn(label: Text('Id Producto')),
                            DataColumn(label: Text('Cantidad')),
                            DataColumn(label: Text('Costo Unitario')),
                            DataColumn(label: Text('Fecha Venc. (YYYY-MM-DD)')),
                            DataColumn(label: Text('')),
                          ],
                          rows: _items.asMap().entries.map((e) {
                            final i = e.key; final it = e.value;
                            return DataRow(cells: [
                              DataCell(SizedBox(width: 120, child: TextField(controller: it.idProducto, keyboardType: TextInputType.number, decoration: const InputDecoration(hintText: 'id')))),
                              DataCell(SizedBox(width: 100, child: TextField(controller: it.cantidad, keyboardType: TextInputType.number, decoration: const InputDecoration(hintText: 'cant')))),
                              DataCell(SizedBox(width: 140, child: TextField(controller: it.costo, keyboardType: const TextInputType.numberWithOptions(decimal: true), decoration: const InputDecoration(hintText: 'costo')))),
                              DataCell(SizedBox(width: 160, child: TextField(controller: it.fechaVenc, decoration: const InputDecoration(hintText: 'YYYY-MM-DD')))),
                              DataCell(IconButton(onPressed: (){ setState(()=>_items.removeAt(i)); }, icon: const Icon(Icons.delete_outline))),
                            ]);
                          }).toList(),
                        ),
                      ),
                      const SizedBox(height: 8),
                      Align(
                        alignment: Alignment.centerRight,
                        child: FilledButton(
                          onPressed: _loading ? null : () async {
                            final idProv = int.tryParse(_idProveedor.text.trim());
                            if (idProv == null) { _snack('ID Proveedor inválido', error: true); return; }
                            if (_items.isEmpty) { _snack('Agrega al menos un ítem', error: true); return; }
                            final items = <Map<String, dynamic>>[];
                            for (final it in _items) {
                              final idP = int.tryParse(it.idProducto.text.trim());
                              final cant = int.tryParse(it.cantidad.text.trim());
                              final costo = double.tryParse(it.costo.text.trim());
                              final fv = it.fechaVenc.text.trim();
                              if (idP == null || cant == null || costo == null) { _snack('Ítem inválido', error: true); return; }
                              items.add({
                                'idProducto': idP,
                                'cantidad': cant,
                                'costoUnitario': costo,
                                if (fv.isNotEmpty) 'fechaVencimiento': fv,
                              });
                            }
                            final body = {
                              'idProveedor': idProv,
                              'condicion': _condicion,
                              'estado': 'ABIERTA',
                              if (_observacion.text.trim().isNotEmpty) 'observacion': _observacion.text.trim(),
                              'items': items,
                            };
                            setState(()=>_loading=true);
                            try {
                              await _svc().crear(body);
                              _snack('Compra registrada');
                              setState(() { _items.clear(); _items.add(_CompraItem()); _observacion.clear(); });
                            } catch (e) { _snack('Error: $e', error: true); }
                            finally { if (mounted) setState(()=>_loading=false);} 
                          },
                          child: _loading ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Guardar compra'),
                        ),
                      ),
                    ],
                  ),
                ),
              ),

              const SizedBox(height: 16),
              Text('Pagar compra', style: Theme.of(context).textTheme.titleMedium),
              const SizedBox(height: 8),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Wrap(spacing: 12, runSpacing: 8, crossAxisAlignment: WrapCrossAlignment.center, children: [
                    SizedBox(width: 160, child: TextField(controller: _idCompra, decoration: const InputDecoration(labelText: 'ID Compra'), keyboardType: TextInputType.number)),
                    SizedBox(width: 160, child: TextField(controller: _montoPago, decoration: const InputDecoration(labelText: 'Monto'), keyboardType: const TextInputType.numberWithOptions(decimal: true))),
                    DropdownButton<String>(
                      value: _metodo,
                      items: const [ DropdownMenuItem(value: 'EFECTIVO', child: Text('EFECTIVO')) ],
                      onChanged: (v) => setState(() => _metodo = v ?? 'EFECTIVO'),
                    ),
                    SizedBox(width: 260, child: TextField(controller: _obsPago, decoration: const InputDecoration(labelText: 'Observación'))),
                    FilledButton(
                      onPressed: _loading ? null : () async {
                        final id = int.tryParse(_idCompra.text.trim());
                        final monto = double.tryParse(_montoPago.text.trim());
                        if (id == null || monto == null) { _snack('Datos de pago inválidos', error: true); return; }
                        setState(()=>_loading=true);
                        try {
                          await _svc().pagar(id, monto, metodo: _metodo, observacion: _obsPago.text.trim().isEmpty ? null : _obsPago.text.trim());
                          _snack('Pago registrado');
                        } catch (e) { _snack('Error: $e', error: true); }
                        finally { if (mounted) setState(()=>_loading=false);} 
                      },
                      child: _loading ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Registrar pago'),
                    ),
                  ]),
                ),
              ),

              const SizedBox(height: 16),
              Text('Reporte de compras', style: Theme.of(context).textTheme.titleMedium),
              const SizedBox(height: 8),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(12),
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
                      FilledButton(
                        onPressed: _loading ? null : () async {
                          setState(() { _loading = true; _error = null; });
                          try { _reporte = await _svc().reporte(_periodo); }
                          catch (e) { _error = e.toString(); }
                          finally { if (mounted) setState(()=>_loading=false);} 
                        },
                        child: const Text('Cargar'),
                      ),
                      OutlinedButton(
                        onPressed: () {
                          final url = _svc().reporteCsvUrl(_periodo);
                          html.window.open(url, '_blank');
                        },
                        child: const Text('Descargar CSV'),
                      ),
                    ]),
                    const SizedBox(height: 8),
                    if (_error != null) Text(_error!, style: const TextStyle(color: Colors.red)),
                    if (_reporte != null)
                      Card(child: ListTile(title: const Text('Resumen Compras'), subtitle: Text('Cantidad: ${_reporte!['cantidad']} | Total: ${_reporte!['total']}'))),
                  ]),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _CompraItem {
  final idProducto = TextEditingController();
  final cantidad = TextEditingController();
  final costo = TextEditingController();
  final fechaVenc = TextEditingController();
}

