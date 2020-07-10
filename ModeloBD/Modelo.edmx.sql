
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 07/10/2020 02:09:52
-- Generated from EDMX file: C:\Users\BETO\Documents\GitHub\ClienteAndroid\ModeloBD\Modelo.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [Aplicaciones];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_ListaDeReproduccioncancion]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[cancion] DROP CONSTRAINT [FK_ListaDeReproduccioncancion];
GO
IF OBJECT_ID(N'[dbo].[FK_Albumcancion]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[cancion] DROP CONSTRAINT [FK_Albumcancion];
GO
IF OBJECT_ID(N'[dbo].[FK_CuentaListaDeReproduccion]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[ListaDeReproduccionSet] DROP CONSTRAINT [FK_CuentaListaDeReproduccion];
GO
IF OBJECT_ID(N'[dbo].[FK_ArtistaAlbum]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Album] DROP CONSTRAINT [FK_ArtistaAlbum];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[Album]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Album];
GO
IF OBJECT_ID(N'[dbo].[cancion]', 'U') IS NOT NULL
    DROP TABLE [dbo].[cancion];
GO
IF OBJECT_ID(N'[dbo].[Cuenta]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Cuenta];
GO
IF OBJECT_ID(N'[dbo].[ListaDeReproduccionSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ListaDeReproduccionSet];
GO
IF OBJECT_ID(N'[dbo].[ArtistaSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ArtistaSet];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'Album'
CREATE TABLE [dbo].[Album] (
    [id] int  NOT NULL,
    [nombre] varchar(50)  NOT NULL,
    [fecha] datetime  NOT NULL,
    [descripcion] varchar(200)  NOT NULL,
    [ArtistaId] int  NULL
);
GO

-- Creating table 'cancion'
CREATE TABLE [dbo].[cancion] (
    [id] int  NOT NULL,
    [nombre] varchar(80)  NOT NULL,
    [genero] varchar(50)  NOT NULL,
    [duracion] varchar(10)  NOT NULL,
    [ListaDeReproduccionId] int  NULL,
    [Album_id] int  NULL
);
GO

-- Creating table 'Cuenta'
CREATE TABLE [dbo].[Cuenta] (
    [id] int  NOT NULL,
    [nombreUsuario] varchar(100)  NOT NULL,
    [correoElectronico] varchar(100)  NOT NULL,
    [contraseña] varchar(100)  NOT NULL,
    [tipo] varchar(45)  NOT NULL
);
GO

-- Creating table 'ListaDeReproduccionSet'
CREATE TABLE [dbo].[ListaDeReproduccionSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [Nombre] nvarchar(max)  NOT NULL,
    [Cuenta_id] int  NOT NULL
);
GO

-- Creating table 'ArtistaSet'
CREATE TABLE [dbo].[ArtistaSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [NombreArtistico] nvarchar(max)  NOT NULL,
    [Descripcion] nvarchar(max)  NOT NULL
);
GO

-- Creating table 'cancionListaDeReproduccion'
CREATE TABLE [dbo].[cancionListaDeReproduccion] (
    [cancion_id] int  NOT NULL,
    [ListaDeReproduccion_Id] int  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [id] in table 'Album'
ALTER TABLE [dbo].[Album]
ADD CONSTRAINT [PK_Album]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'cancion'
ALTER TABLE [dbo].[cancion]
ADD CONSTRAINT [PK_cancion]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Cuenta'
ALTER TABLE [dbo].[Cuenta]
ADD CONSTRAINT [PK_Cuenta]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [Id] in table 'ListaDeReproduccionSet'
ALTER TABLE [dbo].[ListaDeReproduccionSet]
ADD CONSTRAINT [PK_ListaDeReproduccionSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'ArtistaSet'
ALTER TABLE [dbo].[ArtistaSet]
ADD CONSTRAINT [PK_ArtistaSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [cancion_id], [ListaDeReproduccion_Id] in table 'cancionListaDeReproduccion'
ALTER TABLE [dbo].[cancionListaDeReproduccion]
ADD CONSTRAINT [PK_cancionListaDeReproduccion]
    PRIMARY KEY CLUSTERED ([cancion_id], [ListaDeReproduccion_Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [Album_id] in table 'cancion'
ALTER TABLE [dbo].[cancion]
ADD CONSTRAINT [FK_Albumcancion]
    FOREIGN KEY ([Album_id])
    REFERENCES [dbo].[Album]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Albumcancion'
CREATE INDEX [IX_FK_Albumcancion]
ON [dbo].[cancion]
    ([Album_id]);
GO

-- Creating foreign key on [Cuenta_id] in table 'ListaDeReproduccionSet'
ALTER TABLE [dbo].[ListaDeReproduccionSet]
ADD CONSTRAINT [FK_CuentaListaDeReproduccion]
    FOREIGN KEY ([Cuenta_id])
    REFERENCES [dbo].[Cuenta]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_CuentaListaDeReproduccion'
CREATE INDEX [IX_FK_CuentaListaDeReproduccion]
ON [dbo].[ListaDeReproduccionSet]
    ([Cuenta_id]);
GO

-- Creating foreign key on [ArtistaId] in table 'Album'
ALTER TABLE [dbo].[Album]
ADD CONSTRAINT [FK_ArtistaAlbum]
    FOREIGN KEY ([ArtistaId])
    REFERENCES [dbo].[ArtistaSet]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_ArtistaAlbum'
CREATE INDEX [IX_FK_ArtistaAlbum]
ON [dbo].[Album]
    ([ArtistaId]);
GO

-- Creating foreign key on [cancion_id] in table 'cancionListaDeReproduccion'
ALTER TABLE [dbo].[cancionListaDeReproduccion]
ADD CONSTRAINT [FK_cancionListaDeReproduccion_cancion]
    FOREIGN KEY ([cancion_id])
    REFERENCES [dbo].[cancion]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [ListaDeReproduccion_Id] in table 'cancionListaDeReproduccion'
ALTER TABLE [dbo].[cancionListaDeReproduccion]
ADD CONSTRAINT [FK_cancionListaDeReproduccion_ListaDeReproduccion]
    FOREIGN KEY ([ListaDeReproduccion_Id])
    REFERENCES [dbo].[ListaDeReproduccionSet]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_cancionListaDeReproduccion_ListaDeReproduccion'
CREATE INDEX [IX_FK_cancionListaDeReproduccion_ListaDeReproduccion]
ON [dbo].[cancionListaDeReproduccion]
    ([ListaDeReproduccion_Id]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------