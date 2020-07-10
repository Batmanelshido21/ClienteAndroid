using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class Album
    {
        [Key]
        public int id { get; set; }
        public string nombre { get; set; }
        public string fecha { get; set; }
        public string descripcion { get; set; }
        public int ArtistaId { get; set; }
    }
}
