using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class Artista
    {
        [Key]
        public int id { get; set; }
        public string nombreArtistico { get; set; }
        public string descripcion { get; set; }
    }
}
