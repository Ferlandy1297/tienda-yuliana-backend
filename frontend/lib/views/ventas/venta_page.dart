import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../services/venta_service.dart';
import '../../widgets/responsive_scaffold.dart';

class VentaPage extends ConsumerStatefulWidget {
  const VentaPage({super.key});
  @override
  ConsumerState<VentaPage> createState() => _VentaPageState();
}

class _VentaPageState extends ConsumerState<VentaPage> {
  String _tipo = 'DETALLE';
  final _idCliente = TextEditingController();
  final List<_Item> _items = [];
  final _monto = TextEditingController();
  bool _saving = false;

  VentaService _svc() => VentaService(token: ref.read(authProvider).token!);
  void _snack(String m, {bool error=false}) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(m), backgroundColor: error?Colors.red:null));

  @override
  void dispose(){ _idCliente.dispose(); _monto.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Registrar Venta (ADMIN/EMPLEADO)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Row(children: [
              DropdownButton<String>(value: _tipo, items: const [
                DropdownMenuItem(value: 'DETALLE', child: Text('DETALLE')),
                DropdownMenuItem(value: 'MAYOREO', child: Text('MAYOREO')),
                DropdownMenuItem(value: 'FIADO', child: Text('FIADO')),
              ], onChanged: (v)=> setState(()=> _tipo = v ?? 'DETALLE')),
              const SizedBox(width: 8),
              Expanded(child: TextField(controller: _idCliente, decoration: const InputDecoration(labelText: 'ID Cliente (opcional)'), keyboardType: TextInputType.number)),
            ]),
            const SizedBox(height: 12),
            FilledButton.icon(onPressed: ()=> setState(()=> _items.add(_Item())), icon: const Icon(Icons.add), label: const Text('Agregar ítem')),
            const SizedBox(height: 8),
            ..._items.map((it) => Card(child: Padding(padding: const EdgeInsets.all(12), child: Row(children: [
              SizedBox(width:140, child: TextField(controller: it.idProducto, decoration: const InputDecoration(labelText: 'ID Producto (o código)'), keyboardType: TextInputType.number)),
              const SizedBox(width:8),
              SizedBox(width:120, child: TextField(controller: it.cantidad, decoration: const InputDecoration(labelText: 'Cantidad'), keyboardType: TextInputType.number)),
              const SizedBox(width:8),
              SizedBox(width:150, child: TextField(controller: it.precio, decoration: const InputDecoration(labelText: 'Precio unitario (MAYOREO)'), keyboardType: TextInputType.number)),
              const Spacer(),
              IconButton(onPressed: ()=> setState(()=> _items.remove(it)), icon: const Icon(Icons.delete, color: Colors.red)),
            ])))),
            const SizedBox(height: 12),
            Row(children: [
              SizedBox(width: 160, child: TextField(controller: _monto, decoration: const InputDecoration(labelText: 'Monto entregado'), keyboardType: TextInputType.number)),
              const SizedBox(width: 8),
              FilledButton(
                onPressed: _saving ? null : () async {
                  if (_items.isEmpty) { _snack('Agrega ítems', error: true); return; }
                  final items = <Map<String, dynamic>>[];
                  for (final it in _items) {
                    final idP = int.tryParse(it.idProducto.text.trim());
                    final cant = int.tryParse(it.cantidad.text.trim());
                    final price = double.tryParse(it.precio.text.trim());
                    if (idP == null || cant == null) { _snack('Ítem inválido', error: true); return; }
                    items.add({
                      'idProducto': idP,
                      'cantidad': cant,
                      if (_tipo == 'MAYOREO' && price != null) 'precioUnitario': price,
                    });
                  }
                  final body = {
                    'tipo': _tipo,
                    if (_idCliente.text.trim().isNotEmpty) 'idCliente': int.tryParse(_idCliente.text.trim()),
                    'items': items,
                    'pago': {
                      'montoEntregado': double.tryParse(_monto.text.trim()) ?? 0,
                    }
                  };
                  setState(()=>_saving=true);
                  try {
                    await _svc().crear(body);
                    _snack('Venta registrada');
                    setState(() { _items.clear(); _monto.clear(); });
                  } catch (e) { _snack('Error: $e', error: true); }
                  finally { if (mounted) setState(()=>_saving=false);}                
                },
                child: _saving ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Guardar venta'),
              )
            ])
          ]),
        ),
      ),
    );
  }
}

class _Item {
  final idProducto = TextEditingController();
  final cantidad = TextEditingController();
  final precio = TextEditingController();
}

