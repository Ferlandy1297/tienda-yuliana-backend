import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../models/proveedor.dart';
import '../../services/proveedor_service.dart';
import '../../state/auth_notifier.dart';
import '../../widgets/responsive_scaffold.dart';

class ProvidersPage extends ConsumerStatefulWidget {
  const ProvidersPage({super.key});
  @override
  ConsumerState<ProvidersPage> createState() => _ProvidersPageState();
}

class _ProvidersPageState extends ConsumerState<ProvidersPage> {
  bool _loading = false;
  String? _error;
  List<Proveedor> _items = [];
  String _query = '';
  int _page = 0;
  int _rowsPerPage = 10;
  final List<int> _rowsPerPageOptions = const [5, 10, 20, 50];
  int? _sortColumn;
  bool _sortAsc = true;

  ProveedorService _svc() => ProveedorService(token: ref.read(authProvider).token!);

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() { _loading = true; _error = null; });
    try { _items = await _svc().list(); }
    catch (e) { _error = e.toString(); }
    finally { if (mounted) setState(() => _loading = false); }
  }

  @override
  Widget build(BuildContext context) {
    final role = ref.watch(authProvider).role;
    final canEdit = role == 'admin';

    var filtered = _items.where((p) => p.nombre.toLowerCase().contains(_query.toLowerCase())).toList();
    if (_sortColumn != null) {
      int cmp<T extends Comparable>(T a, T b) => a.compareTo(b);
      switch (_sortColumn) {
        case 0: filtered.sort((a, b) => cmp(a.nombre.toLowerCase(), b.nombre.toLowerCase())); break;
        case 1: filtered.sort((a, b) => cmp((a.contacto ?? '').toLowerCase(), (b.contacto ?? '').toLowerCase())); break;
        case 2: filtered.sort((a, b) => cmp((a.telefono ?? '').toLowerCase(), (b.telefono ?? '').toLowerCase())); break;
        case 3: filtered.sort((a, b) => cmp(a.activo ? 1 : 0, b.activo ? 1 : 0)); break;
      }
      if (!_sortAsc) filtered = filtered.reversed.toList();
    }

    final total = filtered.length;
    final maxPage = (total == 0) ? 0 : ((total - 1) / _rowsPerPage).floor();
    if (_page > maxPage) _page = 0;
    final start = (_page * _rowsPerPage).clamp(0, total);
    final end = (start + _rowsPerPage).clamp(0, total);
    final paged = filtered.sublist(start, end);

    Widget content;
    if (_loading) {
      content = const Center(child: CircularProgressIndicator());
    } else if (_error != null) {
      content = Center(child: Text(_error!, style: const TextStyle(color: Colors.red)));
    } else {
      content = Column(children: [
        Row(children: [
          Expanded(
            child: TextField(
              decoration: const InputDecoration(prefixIcon: Icon(Icons.search), hintText: 'Buscar por nombre'),
              onChanged: (q) => setState(() { _query = q; _page = 0; }),
            ),
          ),
          const SizedBox(width: 8),
          if (canEdit)
            FilledButton.icon(
              onPressed: () async {
                final ok = await showDialog<bool>(
                  context: context,
                  builder: (c) => _ProviderForm(onSubmit: (prov) => _svc().create(prov)),
                );
                if (ok == true) _load();
              },
              icon: const Icon(Icons.add), label: const Text('Nuevo'),
            ),
        ]),
        const SizedBox(height: 12),
        if (total == 0)
          Card(child: Padding(padding: const EdgeInsets.all(16), child: Column(mainAxisSize: MainAxisSize.min, children: const [
            Icon(Icons.inbox, size: 48, color: Colors.grey),
            SizedBox(height:8),
            Text('Sin resultados', style: TextStyle(color: Colors.grey)),
          ]))),
        if (total > 0)
          Expanded(
            child: Card(
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: DataTable(
                  sortColumnIndex: _sortColumn,
                  sortAscending: _sortAsc,
                  columns: [
                    DataColumn(label: const Text('Nombre'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                    DataColumn(label: const Text('Contacto'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                    DataColumn(label: const Text('Telefono'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                    DataColumn(label: const Text('Activo'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                    const DataColumn(label: Text('Acciones')),
                  ],
                  rows: paged.map((p) => DataRow(cells: [
                    DataCell(Text(p.nombre)),
                    DataCell(Text(p.contacto ?? '-')),
                    DataCell(Text(p.telefono ?? '-')),
                    DataCell(Icon(p.activo ? Icons.check_circle : Icons.cancel, color: p.activo ? Colors.green : Colors.red)),
                    DataCell(Row(children: [
                      IconButton(icon: const Icon(Icons.visibility), onPressed: null),
                      if (canEdit)
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () async {
                            final ok = await showDialog<bool>(
                              context: context,
                              builder: (c) => _ProviderForm(proveedor: p, onSubmit: (prov) => _svc().update(prov)),
                            );
                            if (ok == true) _load();
                          },
                        ),
                    ])),
                  ])).toList(),
                ),
              ),
            ),
          ),
        const SizedBox(height: 8),
        Row(children: [
          Text(total == 0 ? '0 resultados' : '${start + 1}-${end} de ${total}'),
          const Spacer(),
          const Text('Filas por pagina: '),
          DropdownButton<int>(value: _rowsPerPage, items: _rowsPerPageOptions.map((v) => DropdownMenuItem(value: v, child: Text('$v'))).toList(), onChanged: (v) => setState(() { _rowsPerPage = v ?? 10; _page = 0; })),
          IconButton(tooltip: 'Anterior', onPressed: _page > 0 ? () => setState(() => _page -= 1) : null, icon: const Icon(Icons.chevron_left)),
          Text('${total == 0 ? 0 : (_page + 1)} / ${(maxPage + 1)}'),
          IconButton(tooltip: 'Siguiente', onPressed: (_page < maxPage) ? () => setState(() => _page += 1) : null, icon: const Icon(Icons.chevron_right)),
        ]),
      ]);
    }

    return ResponsiveScaffold(
      title: 'Proveedores',
      body: Padding(padding: const EdgeInsets.all(16), child: content),
    );
  }
}

class _ProviderForm extends StatefulWidget {
  final Proveedor? proveedor;
  final Future<void> Function(Proveedor) onSubmit;
  const _ProviderForm({this.proveedor, required this.onSubmit});
  @override
  State<_ProviderForm> createState() => _ProviderFormState();
}

class _ProviderFormState extends State<_ProviderForm> {
  final _formKey = GlobalKey<FormState>();
  final _nombre = TextEditingController();
  final _contacto = TextEditingController();
  final _telefono = TextEditingController();
  final _direccion = TextEditingController();
  bool _activo = true;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    final p = widget.proveedor;
    if (p != null) {
      _nombre.text = p.nombre;
      _contacto.text = p.contacto ?? '';
      _telefono.text = p.telefono ?? '';
      _direccion.text = p.direccion ?? '';
      _activo = p.activo;
    }
  }

  @override
  void dispose() { _nombre.dispose(); _contacto.dispose(); _telefono.dispose(); _direccion.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.proveedor == null ? 'Nuevo proveedor' : 'Editar proveedor'),
      content: SizedBox(
        width: 420,
        child: Form(
          key: _formKey,
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            TextFormField(controller: _nombre, decoration: const InputDecoration(labelText: 'Nombre'), validator: (v) => (v==null||v.isEmpty)?'Requerido':null),
            TextFormField(controller: _contacto, decoration: const InputDecoration(labelText: 'Contacto')),
            TextFormField(controller: _telefono, decoration: const InputDecoration(labelText: 'Telefono')),
            TextFormField(controller: _direccion, decoration: const InputDecoration(labelText: 'Direccion')),
            Row(children: [
              const Text('Activo'),
              const SizedBox(width: 8),
              Switch(value: _activo, onChanged: (v)=>setState(()=>_activo=v)),
            ]),
          ]),
        ),
      ),
      actions: [
        TextButton(onPressed: _saving ? null : ()=>Navigator.pop(context, false), child: const Text('Cancelar')),
        FilledButton(onPressed: _saving ? null : () async {
          if(!_formKey.currentState!.validate()) return;
          setState(() => _saving = true);
          final p = Proveedor(
            id: widget.proveedor?.id ?? 0,
            nombre: _nombre.text.trim(),
            contacto: _contacto.text.trim().isEmpty? null : _contacto.text.trim(),
            telefono: _telefono.text.trim().isEmpty? null : _telefono.text.trim(),
            direccion: _direccion.text.trim().isEmpty? null : _direccion.text.trim(),
            activo: _activo,
          );
          try { await widget.onSubmit(p); if (mounted) Navigator.pop(context, true); }
          finally { if (mounted) setState(() => _saving = false); }
        }, child: _saving ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Guardar')),
      ],
    );
  }
}
