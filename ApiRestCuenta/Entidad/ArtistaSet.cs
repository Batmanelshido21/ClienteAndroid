using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class ArtistaSet
    {
        [Key]
        public int id { get; set; }
        public string NombreArtistico { get; set; }
        public string Descripcion { get; set; }
    }
}
