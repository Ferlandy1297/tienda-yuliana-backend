import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../models/product.dart';
import '../../services/product_service.dart';
import '../../state/auth_notifier.dart';
import '../../widgets/responsive_scaffold.dart';

class ProductsPage extends ConsumerStatefulWidget {
  const ProductsPage({super.key});
  @override
  ConsumerState<ProductsPage> createState() => _ProductsPageState();
}

class _ProductsPageState extends ConsumerState<ProductsPage> {
  bool _loading = false;
  String? _error;
  List<Product> _items = [];
  String _query = '';
  int _page = 0;
  int _rowsPerPage = 10;
  final List<int> _rowsPerPageOptions = const [5, 10, 20, 50];
  int? _sortColumn;
  bool _sortAsc = true;

  ProductService _svc() => ProductService(token: ref.read(authProvider).token!);

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() { _loading = true; _error = null; });
    try { _items = await _svc().alertasStockBajo(); }
    catch (e) { _error = e.toString(); }
    finally { if (mounted) setState(()=>_loading=false); }
  }

  @override
  Widget build(BuildContext context) {
    final role = ref.watch(authProvider).role;
    final canEdit = role == 'admin';

    var filtered = _items.where((p) => p.nombre.toLowerCase().contains(_query.toLowerCase())).toList();
    int cmp<T extends Comparable>(T a, T b) => a.compareTo(b);
    if (_sortColumn != null) {
      switch (_sortColumn) {
        case 0: filtered.sort((a, b) => cmp(a.nombre.toLowerCase(), b.nombre.toLowerCase())); break;
        case 1: filtered.sort((a, b) => cmp(a.precioVenta, b.precioVenta)); break;
        case 2: filtered.sort((a, b) => cmp(a.stock, b.stock)); break;
        case 3: filtered.sort((a, b) => cmp(a.stockMinimo, b.stockMinimo)); break;
      }
      if (!_sortAsc) filtered = filtered.reversed.toList();
    }

    final total = filtered.length;
    final maxPage = (total == 0) ? 0 : ((total - 1) / _rowsPerPage).floor();
    if (_page > maxPage) _page = 0;
    final start = (_page * _rowsPerPage).clamp(0, total);
    final end = (start + _rowsPerPage).clamp(0, total);
    final paged = filtered.sublist(start, end);

    return ResponsiveScaffold(
      title: 'Productos (alertas stock bajo)',
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: _loading
            ? const Center(child: CircularProgressIndicator())
            : (_error != null
                ? Center(child: Text(_error!, style: const TextStyle(color: Colors.red)))
                : Column(children: [
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
                              builder: (_) => _ProductForm(onSubmit: (p) => _svc().create(p)),
                            );
                            if (ok == true) _load();
                          },
                          icon: const Icon(Icons.add),
                          label: const Text('Crear Producto'),
                        ),
                      const SizedBox(width: 8),
                      if (canEdit)
                        OutlinedButton.icon(
                          onPressed: _loading ? null : () async {
                            try { await _svc().notificarStockBajo(); ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Notificacion enviada'))); }
                            catch (e) { ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error al notificar: $e'), backgroundColor: Colors.red)); }
                          },
                          icon: const Icon(Icons.email),
                          label: const Text('Notificar stock bajo'),
                        ),
                    ]),
                    const SizedBox(height: 12),
                    Expanded(
                      child: LayoutBuilder(builder: (context, constraints) {
                        final columns = constraints.maxWidth > 700
                            ? [
                                DataColumn(label: const Text('Nombre'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                                DataColumn(label: const Text('Precio venta'), numeric: true, onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                                DataColumn(label: const Text('Stock'), numeric: true, onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                                DataColumn(label: const Text('Stock minimo'), numeric: true, onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                                const DataColumn(label: Text('Acciones')),
                              ]
                            : [
                                DataColumn(label: const Text('Nombre'), onSort: (i, asc) => setState(() { _sortColumn = i; _sortAsc = asc; })),
                                const DataColumn(label: Text('Acciones')),
                              ];
                        return Card(
                          child: SingleChildScrollView(
                            scrollDirection: Axis.horizontal,
                            child: DataTable(
                              sortColumnIndex: _sortColumn,
                              sortAscending: _sortAsc,
                              columns: columns,
                              rows: paged.map((p) => DataRow(cells: constraints.maxWidth > 700
                                  ? [
                                      DataCell(Text(p.nombre)),
                                      DataCell(Text(p.precioVenta.toStringAsFixed(2))),
                                      DataCell(Text(p.stock.toString())),
                                      DataCell(Text(p.stockMinimo.toString())),
                                      DataCell(Row(children: [
                                        if (canEdit)
                                          IconButton(
                                            tooltip: 'Actualizar precio',
                                            icon: const Icon(Icons.price_change),
                                            onPressed: () async {
                                              final ok = await showDialog<bool>(
                                                context: context,
                                                builder: (_) => _PriceForm(
                                                  precioActual: p.precioVenta,
                                                  onSubmit: (val) => _svc().updatePrecio(p.idProducto, val),
                                                ),
                                              );
                                              if (ok == true) _load();
                                            },
                                          ),
                                      ])),
                                    ]
                                  : [
                                      DataCell(Text(p.nombre)),
                                      DataCell(Row(children: [
                                        if (canEdit)
                                          IconButton(
                                            tooltip: 'Actualizar precio',
                                            icon: const Icon(Icons.price_change),
                                            onPressed: () async {
                                              final ok = await showDialog<bool>(
                                                context: context,
                                                builder: (_) => _PriceForm(
                                                  precioActual: p.precioVenta,
                                                  onSubmit: (val) => _svc().updatePrecio(p.idProducto, val),
                                                ),
                                              );
                                              if (ok == true) _load();
                                            },
                                          ),
                                      ])),
                                    ])).toList(),
                            ),
                          ),
                        );
                      }),
                    ),
                    const SizedBox(height: 8),
                    Row(children: [
                      Text(total == 0 ? '0 resultados' : '${start + 1}-${end} de ${total}'),
                      const Spacer(),
                      const Text('Filas por pagina: '),
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
                      Text('${total == 0 ? 0 : (_page + 1)} / ${(maxPage + 1)}'),
                      IconButton(
                        tooltip: 'Siguiente',
                        onPressed: (_page < maxPage) ? () => setState(() => _page += 1) : null,
                        icon: const Icon(Icons.chevron_right),
                      ),
                    ])
                  ])),
      ),
    );
  }
}

class _ProductForm extends StatefulWidget {
  final Future<void> Function(Product) onSubmit;
  final Product? product;
  const _ProductForm({required this.onSubmit, this.product});

  @override
  State<_ProductForm> createState() => _ProductFormState();
}

class _ProductFormState extends State<_ProductForm> {
  final _formKey = GlobalKey<FormState>();
  final _nameCtrl = TextEditingController();
  final _priceCtrl = TextEditingController();
  final _stockCtrl = TextEditingController();
  final _stockMinCtrl = TextEditingController();
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    final p = widget.product;
    if (p != null) {
      _nameCtrl.text = p.nombre;
      _priceCtrl.text = p.precioVenta.toString();
      _stockCtrl.text = p.stock.toString();
      _stockMinCtrl.text = p.stockMinimo.toString();
    }
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _priceCtrl.dispose();
    _stockCtrl.dispose();
    _stockMinCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Crear producto'),
      content: SizedBox(
        width: 380,
        child: Form(
          key: _formKey,
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            TextFormField(controller: _nameCtrl, decoration: const InputDecoration(labelText: 'Nombre'), validator: (v) => v == null || v.isEmpty ? 'Requerido' : null),
            TextFormField(controller: _priceCtrl, decoration: const InputDecoration(labelText: 'Precio'), keyboardType: TextInputType.number, validator: (v) => double.tryParse(v ?? '') == null ? 'Numerico' : null),
            TextFormField(controller: _stockCtrl, decoration: const InputDecoration(labelText: 'Stock'), keyboardType: TextInputType.number, validator: (v) => int.tryParse(v ?? '') == null ? 'Entero' : null),
            TextFormField(controller: _stockMinCtrl, decoration: const InputDecoration(labelText: 'Stock minimo'), keyboardType: TextInputType.number, validator: (v) => int.tryParse(v ?? '') == null ? 'Entero' : null),
          ]),
        ),
      ),
      actions: [
        TextButton(onPressed: _saving ? null : () => Navigator.pop(context, false), child: const Text('Cancelar')),
        FilledButton(
          onPressed: _saving ? null : () async {
            if (!_formKey.currentState!.validate()) return;
            setState(() => _saving = true);
            final p = Product(
              idProducto: widget.product?.idProducto ?? 0,
              nombre: _nameCtrl.text.trim(),
              precioVenta: double.parse(_priceCtrl.text.trim()),
              stock: int.parse(_stockCtrl.text.trim()),
              stockMinimo: int.parse(_stockMinCtrl.text.trim()),
            );
            try { await widget.onSubmit(p); if (mounted) Navigator.pop(context, true); }
            finally { if (mounted) setState(() => _saving = false); }
          },
          child: _saving ? const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(strokeWidth: 2)) : const Text('Guardar'),
        )
      ],
    );
  }
}

class _PriceForm extends StatefulWidget {
  final double precioActual;
  final Future<void> Function(double) onSubmit;
  const _PriceForm({required this.precioActual, required this.onSubmit});
  @override
  State<_PriceForm> createState() => _PriceFormState();
}

class _PriceFormState extends State<_PriceForm> {
  final _formKey = GlobalKey<FormState>();
  final _priceCtrl = TextEditingController();
  bool _saving = false;

  @override
  void initState() { super.initState(); _priceCtrl.text = widget.precioActual.toStringAsFixed(2); }
  @override
  void dispose() { _priceCtrl.dispose(); super.dispose(); }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Actualizar precio'),
      content: SizedBox(
        width: 320,
        child: Form(
          key: _formKey,
          child: TextFormField(
            controller: _priceCtrl,
            decoration: const InputDecoration(labelText: 'Precio venta'),
            keyboardType: TextInputType.number,
            validator: (v) => double.tryParse(v ?? '') == null ? 'Numerico' : null,
          ),
        ),
      ),
      actions: [
        TextButton(onPressed: _saving ? null : () => Navigator.pop(context, false), child: const Text('Cancelar')),
        FilledButton(onPressed: _saving ? null : () async {
          if (!_formKey.currentState!.validate()) return;
          setState(() => _saving = true);
          final val = double.parse(_priceCtrl.text.trim());
          try { await widget.onSubmit(val); if (mounted) Navigator.pop(context, true); }
          finally { if (mounted) setState(() => _saving = false); }
        }, child: _saving ? const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(strokeWidth: 2)) : const Text('Guardar')),
      ],
    );
  }
}

