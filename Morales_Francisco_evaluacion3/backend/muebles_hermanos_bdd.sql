-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 13-11-2025 a las 19:33:43
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `muebles_hermanos_bdd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizacion`
--

CREATE TABLE `cotizacion` (
  `id_cotizacion` int(11) NOT NULL,
  `fecha_creacion` datetime NOT NULL,
  `fecha_confirmacion` datetime DEFAULT NULL,
  `estado` varchar(255) NOT NULL,
  `total_final` decimal(38,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cotizacion`
--

INSERT INTO `cotizacion` (`id_cotizacion`, `fecha_creacion`, `fecha_confirmacion`, `estado`, `total_final`) VALUES
(1, '2025-10-25 10:00:00', NULL, 'Cotizado', 366000.00),
(2, '2025-10-26 15:30:00', '2025-10-26 16:00:00', 'Confirmado', 520000.00),
(3, '2025-11-05 15:35:16', '2025-11-05 15:35:26', 'Confirmado', 205000.00),
(4, '2025-11-05 15:48:49', '2025-11-05 15:48:59', 'Confirmado', 350000.00),
(5, '2025-11-05 21:10:01', '2025-11-05 21:10:05', 'Confirmado', 65000.00),
(6, '2025-11-13 18:03:32', '2025-11-13 18:03:38', 'Confirmado', 293000.00),
(7, '2025-11-13 18:20:25', '2025-11-13 18:20:30', 'Confirmado', 850000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizacion_item`
--

CREATE TABLE `cotizacion_item` (
  `id_item` int(11) NOT NULL,
  `cotizacion_id` int(11) NOT NULL,
  `mueble_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_item_total` decimal(38,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cotizacion_item`
--

INSERT INTO `cotizacion_item` (`id_item`, `cotizacion_id`, `mueble_id`, `cantidad`, `precio_item_total`) VALUES
(1, 1, 3, 1, 150000.00),
(2, 2, 2, 1, 450000.00),
(3, 2, 1, 2, 70000.00),
(4, 3, 1, 1, 0.00),
(5, 4, 1, 1, 0.00),
(6, 4, 5, 1, 0.00),
(7, 5, 1, 1, 0.00),
(8, 6, 1, 1, 0.00),
(9, 6, 6, 1, 0.00),
(10, 7, 1, 1, 65000.00),
(11, 7, 5, 1, 245000.00),
(12, 7, 3, 2, 540000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_variante`
--

CREATE TABLE `item_variante` (
  `item_id` int(11) NOT NULL,
  `variante_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `item_variante`
--

INSERT INTO `item_variante` (`item_id`, `variante_id`) VALUES
(1, 1),
(2, 2),
(2, 3),
(3, 1),
(4, 2),
(4, 3),
(5, 2),
(6, 2),
(6, 3),
(7, 4),
(8, 3),
(8, 4),
(9, 4),
(10, 4),
(11, 3),
(11, 4),
(12, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mueble`
--

CREATE TABLE `mueble` (
  `id_mueble` int(11) NOT NULL,
  `nombre_mueble` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `precio_base` decimal(38,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `tamano` varchar(255) NOT NULL,
  `material` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mueble`
--

INSERT INTO `mueble` (`id_mueble`, `nombre_mueble`, `tipo`, `precio_base`, `stock`, `estado`, `tamano`, `material`) VALUES
(1, 'Silla Oslo Estándar', 'Silla', 35000.00, 43, 'Activo', 'Mediano', 'Madera de Pino'),
(2, 'Sillón Relax Vintage', 'Sillón', 280000.00, 14, 'Activo', 'Grande', 'Cuero Sintético'),
(3, 'Mesa Comedor Industrial', 'Mesa', 150000.00, 8, 'Activo', 'Grande', 'Metal y Madera'),
(4, 'Estante Modular Básico', 'Estante', 45000.00, 30, 'Activo', 'Pequeño', 'Melamina'),
(5, 'Cajonera 4 Cajones', 'Cajón', 95000.00, 20, 'Activo', 'Mediano', 'MDF'),
(6, 'Silla Escritorio Ergonómica', 'Silla', 78000.00, 39, 'Activo', 'Mediano', 'Plástico Reforzado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `variante`
--

CREATE TABLE `variante` (
  `id_variante` int(11) NOT NULL,
  `nombre_variante` varchar(255) NOT NULL,
  `precio_adicional` decimal(38,2) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `variante`
--

INSERT INTO `variante` (`id_variante`, `nombre_variante`, `precio_adicional`, `descripcion`) VALUES
(1, 'Normal', 0.00, 'Variante estándar, mantiene el precio base.'),
(2, 'Barniz Premium', 50000.00, 'Añade un barniz de alta calidad.'),
(3, 'Cojines de Seda', 120000.00, 'Incluye cojines con material de seda.'),
(4, 'Ruedas Reforzadas', 30000.00, 'Variante que añade ruedas al mueble.');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  ADD PRIMARY KEY (`id_cotizacion`);

--
-- Indices de la tabla `cotizacion_item`
--
ALTER TABLE `cotizacion_item`
  ADD PRIMARY KEY (`id_item`),
  ADD KEY `cotizacion_id` (`cotizacion_id`),
  ADD KEY `mueble_id` (`mueble_id`);

--
-- Indices de la tabla `item_variante`
--
ALTER TABLE `item_variante`
  ADD PRIMARY KEY (`item_id`,`variante_id`),
  ADD KEY `variante_id` (`variante_id`);

--
-- Indices de la tabla `mueble`
--
ALTER TABLE `mueble`
  ADD PRIMARY KEY (`id_mueble`);

--
-- Indices de la tabla `variante`
--
ALTER TABLE `variante`
  ADD PRIMARY KEY (`id_variante`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  MODIFY `id_cotizacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `cotizacion_item`
--
ALTER TABLE `cotizacion_item`
  MODIFY `id_item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `mueble`
--
ALTER TABLE `mueble`
  MODIFY `id_mueble` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `variante`
--
ALTER TABLE `variante`
  MODIFY `id_variante` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cotizacion_item`
--
ALTER TABLE `cotizacion_item`
  ADD CONSTRAINT `cotizacion_item_ibfk_1` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id_cotizacion`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cotizacion_item_ibfk_2` FOREIGN KEY (`mueble_id`) REFERENCES `mueble` (`id_mueble`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `item_variante`
--
ALTER TABLE `item_variante`
  ADD CONSTRAINT `item_variante_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `cotizacion_item` (`id_item`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `item_variante_ibfk_2` FOREIGN KEY (`variante_id`) REFERENCES `variante` (`id_variante`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
