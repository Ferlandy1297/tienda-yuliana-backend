class Product {
  final int idProducto;
  final String nombre;
  final double precioVenta;
  final int stock;
  final int stockMinimo;

  Product({required this.idProducto, required this.nombre, required this.precioVenta, required this.stock, required this.stockMinimo});

  factory Product.fromJson(Map<String, dynamic> json) => Product(
        idProducto: (json['idProducto'] ?? json['id']).toString().isNotEmpty ? int.parse((json['idProducto'] ?? json['id']).toString()) : 0,
        nombre: (json['nombre'] ?? json['name']).toString(),
        precioVenta: (json['precioVenta'] ?? json['price'] ?? 0).toString() == 'null' ? 0 : (json['precioVenta'] as num).toDouble(),
        stock: (json['stock'] as num?)?.toInt() ?? 0,
        stockMinimo: (json['stockMinimo'] as num?)?.toInt() ?? 0,
      );

  Map<String, dynamic> toCreateJson() => {
        'nombre': nombre,
        'precioVenta': precioVenta,
        'stock': stock,
        'stockMinimo': stockMinimo,
      };
}
