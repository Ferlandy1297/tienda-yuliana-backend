class Proveedor {
  final int id;
  final String nombre;
  final String? contacto;
  final String? telefono;
  final String? direccion;
  final bool activo;

  Proveedor({required this.id, required this.nombre, this.contacto, this.telefono, this.direccion, required this.activo});

  factory Proveedor.fromJson(Map<String, dynamic> json) => Proveedor(
        id: int.parse((json['idProveedor'] ?? json['id'] ?? '0').toString()),
        nombre: (json['nombre'] ?? '').toString(),
        contacto: json['contacto']?.toString(),
        telefono: json['telefono']?.toString(),
        direccion: json['direccion']?.toString(),
        activo: (json['activo'] as bool?) ?? true,
      );

  Map<String, dynamic> toRequest() => {
        'nombre': nombre,
        'contacto': contacto,
        'telefono': telefono,
        'direccion': direccion,
        'activo': activo,
      };
}

