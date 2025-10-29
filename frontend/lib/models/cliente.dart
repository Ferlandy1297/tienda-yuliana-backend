class Cliente {
  final int idCliente;
  final String nombre;
  final String? telefono;
  final String? nit;
  final bool esMayorista;
  final double limiteCredito;
  final String estadoCredito; // ACTIVO | BLOQUEADO
  final bool activo;

  Cliente({
    required this.idCliente,
    required this.nombre,
    this.telefono,
    this.nit,
    required this.esMayorista,
    required this.limiteCredito,
    required this.estadoCredito,
    required this.activo,
  });

  factory Cliente.fromJson(Map<String, dynamic> json) => Cliente(
        idCliente: int.parse((json['idCliente'] ?? json['id'] ?? '0').toString()),
        nombre: (json['nombre'] ?? '').toString(),
        telefono: json['telefono']?.toString(),
        nit: json['nit']?.toString(),
        esMayorista: (json['esMayorista'] as bool?) ?? false,
        limiteCredito: (json['limiteCredito'] is num)
            ? (json['limiteCredito'] as num).toDouble()
            : double.tryParse(json['limiteCredito']?.toString() ?? '0') ?? 0,
        estadoCredito: (json['estadoCredito'] ?? 'ACTIVO').toString(),
        activo: (json['activo'] as bool?) ?? true,
      );

  Map<String, dynamic> toRequest() => {
        'nombre': nombre,
        if (telefono != null) 'telefono': telefono,
        if (nit != null) 'nit': nit,
        'esMayorista': esMayorista,
        'limiteCredito': limiteCredito,
        'estadoCredito': estadoCredito,
        'activo': activo,
      };
}

