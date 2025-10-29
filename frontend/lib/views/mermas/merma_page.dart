import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../services/merma_service.dart';
import '../../widgets/responsive_scaffold.dart';

class MermaPage extends ConsumerStatefulWidget {
  const MermaPage({super.key});
  @override
  ConsumerState<MermaPage> createState() => _MermaPageState();
}

class _MermaPageState extends ConsumerState<MermaPage> {
  final _idProducto = TextEditingController();
  final _idLote = TextEditingController();
  final _cantidad = TextEditingController();
  final _motivo = TextEditingController();
  final _obs = TextEditingController();
  bool _saving = false;

  MermaService _svc() => MermaService(token: ref.read(authProvider).token!);
  void _snack(String m, {bool error=false}) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(m), backgroundColor: error?Colors.red:null));

  @override
  void dispose(){ _idProducto.dispose(); _idLote.dispose(); _cantidad.dispose(); _motivo.dispose(); _obs.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Registrar Merma (ADMIN)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 560),
          child: Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(mainAxisSize: MainAxisSize.min, children: [
                TextField(controller: _idProducto, decoration: const InputDecoration(labelText: 'ID Producto'), keyboardType: TextInputType.number),
                TextField(controller: _idLote, decoration: const InputDecoration(labelText: 'ID Lote (opcional)'), keyboardType: TextInputType.number),
                TextField(controller: _cantidad, decoration: const InputDecoration(labelText: 'Cantidad'), keyboardType: TextInputType.number),
                TextField(controller: _motivo, decoration: const InputDecoration(labelText: 'Motivo (texto)')),
                TextField(controller: _obs, decoration: const InputDecoration(labelText: 'Observación (opcional)')),
                const SizedBox(height: 12),
                FilledButton(
                  onPressed: _saving ? null : () async {
                    final idP = int.tryParse(_idProducto.text.trim());
                    final cant = int.tryParse(_cantidad.text.trim());
                    if (idP == null || cant == null || _motivo.text.trim().isEmpty) { _snack('Datos inválidos', error: true); return; }
                    setState(()=>_saving=true);
                    try {
                      await _svc().registrar(idProducto: idP, idLote: int.tryParse(_idLote.text.trim()), cantidad: cant, motivo: _motivo.text.trim(), observacion: _obs.text.trim().isEmpty? null : _obs.text.trim());
                      _snack('Merma registrada');
                      _idProducto.clear(); _idLote.clear(); _cantidad.clear(); _motivo.clear(); _obs.clear();
                    } catch (e) { _snack('Error: $e', error: true); }
                    finally { if (mounted) setState(()=>_saving=false);}                  
                  },
                  child: _saving ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Guardar'),
                )
              ]),
            ),
          ),
        ),
      ),
    );
  }
}

