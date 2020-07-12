using ApiRestCuenta.Entidad;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.DBContext
{
    public class Context: DbContext
    {
        public Context(DbContextOptions<Context> options):base(options)
        {
        }

        public DbSet<Cuenta> Cuenta { get; set; }
        public DbSet<Album> Album{get;set;}
        public DbSet<ArtistaSet> ArtistaSet{get;set;}
        public DbSet<Cancion> Cancion{get;set;}
        public DbSet<ListaDeReproduccionSet> ListaDeReproduccionSet{get;set;}

        public DbSet<cancionListaDeReproduccion> CancionListaDeReproduccion{get;set;}
    }
}
