using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.DAO
{
 public class CancionSubida
    {

        public CancionSubida(int id, string nombre,string genero,string duracion, int albumId){
            this.id=id;
            this.nombre=nombre;
            this.genero=genero;
            this.duracion=duracion;
            this.idAlbum = albumId;
        }

        public int id { get; set; }
        public string nombre { get; set; }
        public string genero { get; set; }
        public String duracion { get; set; }
        public string audio { get; set; }
        public int idAlbum { get; set; }
    }
}