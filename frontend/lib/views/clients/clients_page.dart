import 'package:flutter/material.dart';
// ignore: avoid_web_libraries_in_flutter
import 'dart:html' as html;
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../models/cliente.dart';
import '../../services/cliente_service.dart';
import '../../state/auth_notifier.dart';
import '../../widgets/responsive_scaffold.dart';

class ClientsPage extends ConsumerStatefulWidget {
  const ClientsPage({super.key});
  @override
  ConsumerState<ClientsPage> createState() => _ClientsPageState();
}

class _ClientsPageState extends ConsumerState<ClientsPage> {
  bool _loading = false;
  String? _error;
  List<Cliente> _items = [];
  String _query = '';
  String _estadoFilter = 'TODOS'; // TODOS | ACTIVO | BLOQUEADO
  String _mayoristaFilter = 'TODOS'; // TODOS | SI | NO
  int? _sortColumn;
  bool _sortAsc = true;
  int _page = 0;
  int _rowsPerPage = 10;
  final List<int> _rowsPerPageOptions = const [5, 10, 20, 50];

  ClienteService _service() => ClienteService(token: ref.read(authProvider).token!);

  Future<void> _load() async {
    setState(() { _loading = true; _error = null; });
    try { _items = await _service().list(); } catch (e) { _error = e.toString(); } finally { if (mounted) setState(()=>_loading=false);}    
  }

  @override
  void initState() { super.initState(); _load(); }

  void _snack(BuildContext context, String msg, {bool error = false}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(msg), backgroundColor: error ? Colors.red : null),
    );
  }

  void _exportCsv(List<Cliente> data) {
    final headers = [
      'Nombre','Telefono','NIT','Mayorista','LimiteCredito','EstadoCredito','Activo'
    ];
    String escape(String s) {
      final needsQuotes = s.contains(',') || s.contains('\n') || s.contains('"');
      var out = s.replaceAll('"', '""');
      return needsQuotes ? '"$out"' : out;
    }
    final rows = <String>[];
    rows.add(headers.join(','));
    for (final c in data) {
      rows.add([
        escape(c.nombre),
        escape(c.telefono ?? ''),
        escape(c.nit ?? ''),
        c.esMayorista ? 'SI' : 'NO',
        c.limiteCredito.toStringAsFixed(2),
        c.estadoCredito,
        c.activo ? 'SI' : 'NO',
      ].join(','));
    }
    final content = rows.join('\n');
    final bytes = html.Blob([content], 'text/csv');
    final url = html.Url.createObjectUrlFromBlob(bytes);
    final anchor = html.AnchorElement(href: url)
      ..download = 'clientes.csv'
      ..style.display = 'none';
    html.document.body!.children.add(anchor);
    anchor.click();
    anchor.remove();
    html.Url.revokeObjectUrl(url);
  }

  @override
  Widget build(BuildContext context) {
    final role = ref.watch(authProvider).role;
    final canEdit = role == 'admin';
    var filtered = _items.where((c) {
      if (!c.nombre.toLowerCase().contains(_query.toLowerCase())) return false;
      if (_estadoFilter != 'TODOS' && c.estadoCredito.toUpperCase() != _estadoFilter) return false;
      if (_mayoristaFilter == 'SI' && !c.esMayorista) return false;
      if (_mayoristaFilter == 'NO' && c.esMayorista) return false;
      return true;
    }).toList();

    // Ordenamiento
    int cmp<T extends Comparable>(T a, T b) => a.compareTo(b);
    if (_sortColumn != null) {
      switch (_sortColumn) {
        case 0:
          filtered.sort((a, b) => cmp(a.nombre.toLowerCase(), b.nombre.toLowerCase()));
          break;
        case 1:
          filtered.sort((a, b) => cmp((a.telefono ?? '').toLowerCase(), (b.telefono ?? '').toLowerCase()));
          break;
        case 2:
          filtered.sort((a, b) => cmp((a.nit ?? '').toLowerCase(), (b.nit ?? '').toLowerCase()));
          break;
        case 3:
          filtered.sort((a, b) => cmp(a.esMayorista ? 1 : 0, b.esMayorista ? 1 : 0));
          break;
        case 4:
          filtered.sort((a, b) => cmp(a.limiteCredito, b.limiteCredito));
          break;
        case 5:
          filtered.sort((a, b) => cmp(a.estadoCredito.toLowerCase(), b.estadoCredito.toLowerCase()));
          break;
        case 6:
          filtered.sort((a, b) => cmp(a.activo ? 1 : 0, b.activo ? 1 : 0));
          break;
      }
      if (!_sortAsc) {
        filtered = filtered.reversed.toList();
      }
    }
    final total = filtered.length;
    final totalCredito = filtered.fold<double>(0, (acc, c) => acc + c.limiteCredito);
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
              Expanded(child: TextField(
                decoration: const InputDecoration(prefixIcon: Icon(Icons.search), hintText: 'Buscar por nombre'),
                onChanged: (q) => setState(() { _query = q; _page = 0; }),
              )),
              const SizedBox(width: 8),
              DropdownButton<String>(
                value: _estadoFilter,
                items: const [
                  DropdownMenuItem(value: 'TODOS', child: Text('Estado: Todos')),
                  DropdownMenuItem(value: 'ACTIVO', child: Text('Estado: ACTIVO')),
                  DropdownMenuItem(value: 'BLOQUEADO', child: Text('Estado: BLOQUEADO')),
                ],
                onChanged: (v) => setState(() { _estadoFilter = v ?? 'TODOS'; _page = 0; }),
              ),
              const SizedBox(width: 8),
              DropdownButton<String>(
                value: _mayoristaFilter,
                items: const [
                  DropdownMenuItem(value: 'TODOS', child: Text('Mayorista: Todos')),
                  DropdownMenuItem(value: 'SI', child: Text('Mayorista: SÃ­')),
                  DropdownMenuItem(value: 'NO', child: Text('Mayorista: No')),
                ],
                onChanged: (v) => setState(() { _mayoristaFilter = v ?? 'TODOS'; _page = 0; }),
              ),
              const Spacer(),
              OutlinedButton.icon(
                onPressed: () => _exportCsv(filtered),
                icon: const Icon(Icons.download),
                label: const Text('Exportar CSV'),
              ),
              const SizedBox(width: 8),
              if (canEdit) FilledButton.icon(
                onPressed: () async {
                  final ok = await showDialog<bool>(
                    context: context,
                    builder: (_) => _ClientForm(
                      onSubmit: (c) => _service().create(c),
                    ),
                  );
                  if (ok == true) {
                    _snack(context, 'Cliente creado');
                    _load();
                  }
                },
                icon: const Icon(Icons.add), label: const Text('Nuevo'),
              ),
            ]),
            const SizedBox(height: 12),
            if (total == 0)
              Expanded(
                child: Card(
                  child: Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.inbox, size: 48, color: Colors.grey),
                        const SizedBox(height: 8),
                        const Text('Sin resultados', style: TextStyle(fontSize: 16, color: Colors.grey)),
                        const SizedBox(height: 8),
                        OutlinedButton.icon(
                          onPressed: () => setState(() { _query = ''; _estadoFilter = 'TODOS'; _mayoristaFilter = 'TODOS'; _page = 0; _sortColumn = null; _sortAsc = true; }),
                          icon: const Icon(Icons.filter_alt_off),
                          label: const Text('Limpiar filtros'),
                        ),
                      ],
                    ),
                  ),
                ),
              )
            else
              Expanded(
                child: Card(
                  child: SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: DataTable(
                      sortColumnIndex: _sortColumn,
                      sortAscending: _sortAsc,
                      columns: [
                        DataColumn(label: const Text('Nombre'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('TelÃ©fono'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('NIT'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('Mayorista'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('LÃ­mite CrÃ©dito'), numeric: true, onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('Estado CrÃ©dito'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        DataColumn(label: const Text('Activo'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                        const DataColumn(label: Text('Acciones')),
                      ],
                      rows: paged.map((c) => DataRow(cells: [
                        DataCell(Text(c.nombre)),
                        DataCell(Text(c.telefono ?? '-')),
                        DataCell(Text(c.nit ?? '-')),
                        DataCell(Icon(c.esMayorista ? Icons.check_circle : Icons.cancel, color: c.esMayorista ? Colors.green : Colors.red)),
                        DataCell(Text(c.limiteCredito.toStringAsFixed(2))),
                        DataCell(Text(c.estadoCredito)),
                        DataCell(Icon(c.activo ? Icons.check_circle : Icons.cancel, color: c.activo ? Colors.green : Colors.red)),
                        DataCell(Row(children: [
                        if (canEdit) IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () async {
                            final ok = await showDialog<bool>(
                              context: context,
                              builder: (_) => _ClientForm(
                                cliente: c,
                                onSubmit: (cli) => _service().update(cli),
                              ),
                            );
                            if (ok == true) {
                              _snack(context, 'Cliente actualizado');
                              _load();
                            }
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
              Text('$total resultados | Total crÃ©dito: ' + totalCredito.toStringAsFixed(2)),
              const Spacer(),
              const Text('Filas por pÃ¡gina: '),
              DropdownButton<int>(
                value: _rowsPerPage,
                items: _rowsPerPageOptions.map((v) => DropdownMenuItem(value: v, child: Text('$v'))).toList(),
                onChanged: (v) => setState(() { _rowsPerPage = v ?? 10; _page = 0; }),
              ),
              IconButton(
                tooltip: 'Anterior',
                onPressed: _page > 0 ? () => setState(() => _page -= 1) : null,
                icon: const Icon(Icons.chevron_left),
              ),
              Text('${total == 0 ? 0 : (_page + 1)} / ${maxPage + 1}'),
              IconButton(
                tooltip: 'Siguiente',
                onPressed: (_page < maxPage) ? () => setState(() => _page += 1) : null,
                icon: const Icon(Icons.chevron_right),
              ),
            ])
          ]);
    }
    return ResponsiveScaffold(
      title: 'Clientes',
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: content,
      ),
    );
  }
}

class _ClientForm extends StatefulWidget {
  final Cliente? cliente;
  final Future<void> Function(Cliente) onSubmit;
  const _ClientForm({this.cliente, required this.onSubmit});
  @override
  State<_ClientForm> createState() => _ClientFormState();
}

class _ClientFormState extends State<_ClientForm> {
  final _formKey = GlobalKey<FormState>();
  final _nombre = TextEditingController();
  final _telefono = TextEditingController();
  final _nit = TextEditingController();
  bool _esMayorista = false;
  final _limite = TextEditingController(text: '0');
  String _estado = 'ACTIVO';
  bool _activo = true;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    final c = widget.cliente;
    if (c != null) {
      _nombre.text = c.nombre;
      _telefono.text = c.telefono ?? '';
      _nit.text = c.nit ?? '';
      _esMayorista = c.esMayorista;
      _limite.text = c.limiteCredito.toStringAsFixed(2);
      _estado = c.estadoCredito;
      _activo = c.activo;
    }
  }

  @override
  void dispose() { _nombre.dispose(); _telefono.dispose(); _nit.dispose(); _limite.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.cliente == null ? 'Nuevo cliente' : 'Editar cliente'),
      content: SizedBox(
        width: 480,
        child: Form(
          key: _formKey,
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            TextFormField(controller: _nombre, decoration: const InputDecoration(labelText: 'Nombre'), validator: (v)=> (v==null||v.isEmpty)?'Requerido':null),
            TextFormField(controller: _telefono, decoration: const InputDecoration(labelText: 'TelÃ©fono')),
            TextFormField(controller: _nit, decoration: const InputDecoration(labelText: 'NIT')),
            SwitchListTile(title: const Text('Mayorista'), value: _esMayorista, onChanged: (v)=>setState(()=>_esMayorista=v)),
            TextFormField(controller: _limite, decoration: const InputDecoration(labelText: 'LÃ­mite de crÃ©dito'), keyboardType: TextInputType.number, validator: (v)=> double.tryParse(v??'')==null? 'NumÃ©rico': null),
            DropdownButtonFormField<String>(
              value: _estado,
              items: const [DropdownMenuItem(value:'ACTIVO', child: Text('ACTIVO')), DropdownMenuItem(value:'BLOQUEADO', child: Text('BLOQUEADO'))],
              onChanged: (v)=> setState(()=> _estado = v ?? 'ACTIVO'),
              decoration: const InputDecoration(labelText: 'Estado crÃ©dito'),
            ),
            SwitchListTile(title: const Text('Activo'), value: _activo, onChanged: (v)=>setState(()=>_activo=v)),
          ]),
        ),
      ),
      actions: [
        TextButton(onPressed: _saving ? null : ()=>Navigator.pop(context, false), child: const Text('Cancelar')),
        FilledButton(
          onPressed: _saving ? null : () async {
            if(!_formKey.currentState!.validate()) return;
            setState(() => _saving = true);
            final c = Cliente(
              idCliente: widget.cliente?.idCliente ?? 0,
              nombre: _nombre.text.trim(),
              telefono: _telefono.text.trim().isEmpty? null : _telefono.text.trim(),
              nit: _nit.text.trim().isEmpty? null : _nit.text.trim(),
              esMayorista: _esMayorista,
              limiteCredito: double.parse(_limite.text.trim()),
              estadoCredito: _estado,
              activo: _activo,
            );
            try {
              await widget.onSubmit(c);
              if (mounted) Navigator.pop(context, true);
            } finally {
              if (mounted) setState(() => _saving = false);
            }
          }, child: _saving ? const SizedBox(height:20,width:20,child:CircularProgressIndicator(strokeWidth:2)) : const Text('Guardar')),
      ],
    );
  }
}



