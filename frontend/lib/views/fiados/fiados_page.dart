import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../state/auth_notifier.dart';
import '../../services/fiado_service.dart';
import '../../widgets/responsive_scaffold.dart';

class FiadosPage extends ConsumerStatefulWidget {
  const FiadosPage({super.key});
  @override
  ConsumerState<FiadosPage> createState() => _FiadosPageState();
}

class _FiadosPageState extends ConsumerState<FiadosPage> {
  final _idClienteCtrl = TextEditingController();
  Map<String, dynamic>? _saldo;
  bool _loading = false;
  String? _error;

  FiadoService _svc() => FiadoService(token: ref.read(authProvider).token!);

  void _snack(String m, {bool error=false}) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(m), backgroundColor: error?Colors.red:null));

  @override
  void dispose() { _idClienteCtrl.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return ResponsiveScaffold(
      title: 'Fiados (ADMIN/EMPLEADO)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Row(children: [
            SizedBox(width: 140, child: TextField(controller: _idClienteCtrl, decoration: const InputDecoration(labelText: 'ID Cliente'), keyboardType: TextInputType.number)),
            const SizedBox(width: 8),
            FilledButton(onPressed: _loading?null:() async {
              final id = int.tryParse(_idClienteCtrl.text.trim());
              if (id == null) { _snack('ID inválido', error: true); return; }
              setState(() { _loading=true; _error=null; });
              try { _saldo = await _svc().saldo(id); } catch (e) { _error = e.toString(); }
              finally { if(mounted) setState(()=>_loading=false);}            
            }, child: const Text('Consultar saldo')),
          ]),
          const SizedBox(height: 12),
          if (_error != null) Text(_error!, style: const TextStyle(color: Colors.red)),
          if (_saldo != null)
            Card(child: ListTile(title: Text('Saldo: ${_saldo!['saldo']}'), subtitle: Text('Estado: ${_saldo!['estado']}'))),
          const SizedBox(height: 12),
          if (_saldo != null)
            Row(children: [
              const Text('Abonar:'), const SizedBox(width: 8),
              SizedBox(width: 120, child: TextField(decoration: const InputDecoration(labelText: 'Monto'), keyboardType: TextInputType.number, onSubmitted: (v) async {
                final monto = double.tryParse(v.trim());
                final id = int.tryParse(_idClienteCtrl.text.trim());
                if (monto == null || id == null) { _snack('Datos inválidos', error: true); return; }
                setState(()=>_loading=true);
                try { _saldo = await _svc().abonar(idCliente: id, monto: monto); _snack('Abono registrado'); }
                catch (e) { _snack('Error: $e', error: true); }
                finally { if (mounted) setState(()=>_loading=false);}              
              })),
            ]),
        ]),
      ),
    );
  }
}

