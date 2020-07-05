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
       
    }
}
