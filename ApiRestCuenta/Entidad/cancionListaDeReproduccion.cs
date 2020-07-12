using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class cancionListaDeReproduccion
    {
        [Key]
        public int cancion_id { get; set; }
        public int ListaDeReproduccion_Id { get; set; }
    }
}
