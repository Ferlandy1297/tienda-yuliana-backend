import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../services/devolucion_service.dart';
import '../../widgets/responsive_scaffold.dart';

class DevolucionPage extends ConsumerStatefulWidget {
  const DevolucionPage({super.key});
  @override
  ConsumerState<DevolucionPage> createState() => _DevolucionPageState();
}

class _DevolucionPageState extends ConsumerState<DevolucionPage> {
  final _idProveedor = TextEditingController();
  final _motivo = TextEditingController();
  final List<_Item> _items = [];
  bool _saving = false;

  DevolucionService _svc() => DevolucionService(token: ref.read(authProvider).token!);
  void _snack(String m, {bool error=false}) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(m), backgroundColor: error?Colors.red:null));

  @override
  void dispose(){ _idProveedor.dispose(); _motivo.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Devolución a Proveedor (ADMIN)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Row(children: [
              SizedBox(width: 160, child: TextField(controller: _idProveedor, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'ID Proveedor'))),
              const SizedBox(width: 8),
              Expanded(child: TextField(controller: _motivo, decoration: const InputDecoration(labelText: 'Motivo (opcional)'))),
            ]),
            const SizedBox(height: 12),
            FilledButton.icon(onPressed: ()=> setState(()=> _items.add(_Item())), icon: const Icon(Icons.add), label: const Text('Agregar ítem')),
            const SizedBox(height: 8),
            ..._items.map((it) => Card(child: Padding(padding: const EdgeInsets.all(12), child: Row(children: [
              SizedBox(width:110, child: TextField(controller: it.idProducto, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'ID Producto'))),
              const SizedBox(width: 8),
              SizedBox(width:110, child: TextField(controller: it.idLote, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'ID Lote'))),
              const SizedBox(width: 8),
              SizedBox(width:110, child: TextField(controller: it.cantidad, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Cantidad'))),
              const SizedBox(width: 8),
              SizedBox(width:130, child: TextField(controller: it.costo, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'Costo estimado'))),
              const Spacer(),
              IconButton(onPressed: ()=> setState(()=> _items.remove(it)), icon: const Icon(Icons.delete, color: Colors.red)),
            ])))),
            const SizedBox(height: 12),
            FilledButton(
              onPressed: _saving ? null : () async {
                final prov = int.tryParse(_idProveedor.text.trim());
                if (prov == null || _items.isEmpty) { _snack('Proveedor e ítems requeridos', error: true); return; }
                final items = <Map<String, dynamic>>[];
                for (final it in _items) {
                  final idP = int.tryParse(it.idProducto.text.trim());
                  final idL = int.tryParse(it.idLote.text.trim());
                  final cant = int.tryParse(it.cantidad.text.trim());
                  final costo = double.tryParse(it.costo.text.trim());
                  if (idP == null || idL == null || cant == null || costo == null) { _snack('Ítem inválido', error: true); return; }
                  items.add({'idProducto': idP, 'idLote': idL, 'cantidad': cant, 'costoEstimado': costo});
                }
                setState(()=>_saving=true);
                try {
                  await _svc().crear(idProveedor: prov, motivo: _motivo.text.trim().isEmpty? null : _motivo.text.trim(), items: items);
                  _snack('Devolución creada');
                  setState(()=> _items.clear());
                } catch (e) { _snack('Error: $e', error: true); }
                finally { if (mounted) setState(()=>_saving=false);}              
              },
              child: _saving ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Registrar'),
            )
          ]),
        ),
      ),
    );
  }
}

class _Item {
  final idProducto = TextEditingController();
  final idLote = TextEditingController();
  final cantidad = TextEditingController();
  final costo = TextEditingController();
}

