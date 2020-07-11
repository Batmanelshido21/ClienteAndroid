using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace ApiRestCuenta.Entidad
{
    public class Cancion
    {
        
        public Cancion(int id, string nombre,string genero,string duracion, int albumId){
            this.id=id;
            this.nombre=nombre;
            this.genero=genero;
            this.duracion=duracion;
            this.Album_id = albumId;
        }

         public Cancion(){
        }

        [Key]
        public int id { get; set; }
        public string nombre { get; set; }
        public string genero { get; set; }
        public string duracion { get; set; }
        public int ListaDeReproduccionId { get; set; }
        public int Album_id { get; set; }

    }
}