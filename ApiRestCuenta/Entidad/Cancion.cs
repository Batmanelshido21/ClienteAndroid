using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class Cancion
    {
        [Key]
        public int id { get; set; }
        public string nombre { get; set; }
        public string genero { get; set; }
        public string duracion { get; set; }
    }
}