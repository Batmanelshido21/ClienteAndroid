using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class ListaDeReproduccionSet
    {
        [Key]
        public int Id { get; set; }
        public string Nombre { get; set; }
        public int Cuenta_id { get; set; }

    }
}
