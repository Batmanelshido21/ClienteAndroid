using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ApiRestCuenta.DBContext;
using ApiRestCuenta.Entidad;
using Microsoft.AspNetCore.Mvc;
using System.Media;
using System.IO;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace ApiRestCuenta.Controllers
{
    [Route("api/[controller]")]
    public class CuentaController : Controller
    {
        private readonly Context context;
       

        public CuentaController(Context context)
        {
            this.context = context;
        }

        // GET: api/<controller>
        [HttpGet]
        public IEnumerable<Cuenta> Get()
        {
            return context.Cuenta.ToList();
        }

        // GET api/<controller>/5
        [HttpGet("login")]
        public Cuenta GetLogin(string nombreUsuario, string contraseña)
        {
            var cuenta = context.Cuenta.Where(x => x.nombreUsuario == nombreUsuario && x.contraseña == contraseña).FirstOrDefault();

            return cuenta;
        }

        [HttpGet("reproducirAudio")]
        public Audio obtenerCancion(string nombreCancion){
        Audio audio = new Audio();
        byte[] cancion;
        byte[] buffer =null;
        int longitud;
        var PathfileName = string.Empty;
        PathfileName = "/home/javier/Descargas/"+nombreCancion+".mp3";
        using (var fs = new FileStream(PathfileName, FileMode.Open, FileAccess.Read))
        {
            buffer = new byte[fs.Length];
            fs.Read(buffer, 0, (int)fs.Length);
            longitud = (int)fs.Length;
        }
        cancion=buffer;
        audio.id=1;
        string vuelta = Convert.ToBase64String(cancion);
        audio.cancion=vuelta;

        return audio;
        }


        [HttpGet("listaAlbum")]
         public IEnumerable<Album> GetAlbums()
        {
            return context.Album.ToList();
        }

        [HttpGet("listaArtistas")]
        public IEnumerable<Artista> GetArtistas(){
            return context.Artista.ToList();
        }

        [HttpGet("listaCanciones")]
        public IEnumerable<Cancion> getCanciones(){
            return context.Cancion.ToList();
        }
        // POST api/<controller>
        [HttpPost("registroCuenta")]
        public Cuenta Post(int id,string nombreUsuario,string correoElectronico,string contraseña,string tipo)
        {
            Cuenta cuenta= new Cuenta();
            cuenta.nombreUsuario=nombreUsuario;
            cuenta.id=id;
            cuenta.correoElectronico=correoElectronico;
            cuenta.contraseña=contraseña;
            cuenta.tipo=tipo;
            try
            {
                context.Cuenta.Add(cuenta);
                context.SaveChanges();

                return cuenta;
            }
            catch (Exception)
            {
                return cuenta;
            }
        }


        [HttpPost("registroArtista")]
        public Artista PostArtista(int id, string nombreArtistico, string descripcion)
        {
            Artista artista = new Artista();
            artista.id=id;
            artista.nombreArtistico=nombreArtistico;
            artista.descripcion=descripcion;
            try
            {
                
                context.Artista.Add(artista);
                context.SaveChanges();

                return artista;
            }
            catch (Exception)
            {
                return artista;
            }
        }


        [HttpPost("registroAlbum")]
        public Album PostAlbum(int id, string nombre, string fecha, string descripcion)
        {
            Album album = new Album();
            album.id=id;
            album.nombre=nombre;
            album.fecha=fecha;
            album.descripcion=descripcion;
            try
            {
                context.Album.Add(album);
                context.SaveChanges();

                return album;
            }
            catch (Exception)
            {
                return album;
            }
        }


        [HttpPost("registroCancion")]
        public Cancion PostCancion(int id, string nombre, string genero, string duracion)
        {
            Cancion cancion = new Cancion();
            cancion.id=id;
            cancion.nombre=nombre;
            cancion.genero=genero;
            cancion.duracion=duracion;
            try
            {
                context.Cancion.Add(cancion);
                context.SaveChanges();

                return cancion;
            }
            catch (Exception)
            {
                return cancion;
            }
        }
        // PUT api/<controller>/5
        [HttpPut]
        public ActionResult Put([FromBody]Cuenta cuenta)
        {
            try
            {
                context.Entry(cuenta).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
            }
        }

        // DELETE api/<controller>/5
        [HttpDelete]
        public ActionResult Delete([FromBody]Cuenta cuenta)
        {
            try
            {
                context.Entry(cuenta).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
            }

        }
    }
}
