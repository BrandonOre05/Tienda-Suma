drop database if exists sistema_tiendasuma;
create database if not exists sistema_tiendasuma;
use sistema_tiendasuma;

-- Tabla usuarios (SIN columna username)
create table usuarios(
    id_usuario int primary key not null auto_increment,
    nombre varchar(60) not null,
    apellido varchar(60) not null,
    telefono varchar(15),
    direccion varchar(100),
    email varchar(60) not null unique,
    password varchar(60) not null,
    rol varchar(45) not null,
    fecha_creacion datetime default current_timestamp,
    estado boolean not null
);

-- Tabla categorias
create table categorias(
    id_categoria int primary key auto_increment not null,
    nombre_categoria varchar(50) not null unique,
    descripcion text
);

-- Tabla proveedores
create table proveedores(
    id_proveedor int primary key auto_increment not null,
    nombre_proveedor varchar(100) not null,
    telefono varchar(15),
    email varchar(60),
    direccion varchar(100),
    estado boolean not null
);

-- Tabla productos
create table productos(
    id_producto int primary key auto_increment not null,
    nombre_producto varchar(60) not null,
    descripcion text,
    precio decimal(10,2) not null,
    stock int not null,
    stock_minimo int default 5,
    categorias_id_categoria int not null,
    proveedores_id_proveedor int not null,
    estado boolean not null,
    constraint fk_productos_categoria foreign key(categorias_id_categoria) references categorias(id_categoria) on update cascade on delete cascade,
    constraint fk_productos_proveedor foreign key(proveedores_id_proveedor) references proveedores(id_proveedor) on update cascade on delete cascade
);

-- Tabla compras
create table compras(
    id_compra int primary key auto_increment not null,
    fecha_compra datetime default current_timestamp,
    total decimal(10,2) default 0.00,
    factura_numero varchar(50),
    proveedores_id_proveedor int not null,
    usuarios_id_usuario int not null,
    constraint fk_compras_proveedor foreign key(proveedores_id_proveedor) references proveedores(id_proveedor) on update cascade on delete cascade,
    constraint fk_compras_usuario foreign key(usuarios_id_usuario) references usuarios(id_usuario) on update cascade on delete cascade
);

-- Tabla detalle_compra
create table detalle_compra(
    id_detalle_compra int primary key auto_increment not null,
    cantidad int not null,
    precio_compra decimal(10,2) not null,
    productos_id_producto int not null,
    compras_id_compra int not null,
    unique(compras_id_compra, productos_id_producto),
    constraint fk_detallecompra_producto foreign key(productos_id_producto) references productos(id_producto) on update cascade on delete cascade,
    constraint fk_detallecompra_compra foreign key(compras_id_compra) references compras(id_compra) on update cascade on delete cascade
);

-- Tabla ventas
create table ventas(
    id_venta int primary key not null auto_increment,
    fecha_venta datetime default current_timestamp,
    total decimal(10,2) not null,
    usuarios_id_usuario int not null,
    vendedor_id_usuario int not null,
    constraint fk_ventas_cliente foreign key(usuarios_id_usuario) references usuarios(id_usuario) on update cascade on delete cascade,
    constraint fk_ventas_vendedor foreign key(vendedor_id_usuario) references usuarios(id_usuario) on update cascade on delete cascade
);

-- Tabla detalle_venta
create table detalle_venta(
    id_detalle_venta int primary key not null auto_increment,
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    productos_id_producto int not null,
    ventas_id_venta int not null,
    unique(ventas_id_venta, productos_id_producto),
    constraint fk_detalleventa_producto foreign key(productos_id_producto) references productos(id_producto) on update cascade on delete cascade,
    constraint fk_detalleventa_venta foreign key(ventas_id_venta) references ventas(id_venta) on update cascade on delete cascade
);