using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.DAO
{
 public class ArtistaDAO
    {
        public int id { get; set; }
        public string nombreArtistico { get; set; }
        public string descripcion { get; set; }
        public string imagen { get; set; }
    }
}