using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.DAO
{
 public class AlbumDAO
    {
        public int id { get; set; }
        public string nombre { get; set; }
        public string fecha { get; set; }
        public string descripcion { get; set; }
        public string imagen { get; set; }
        public int idArtista { get; set; }
    }
}