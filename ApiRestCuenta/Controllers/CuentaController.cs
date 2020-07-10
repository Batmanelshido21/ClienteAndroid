using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ApiRestCuenta.DBContext;
using ApiRestCuenta.Entidad;
using Microsoft.AspNetCore.Mvc;
using System.Media;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using ApiRestCuenta.DAO;

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
        public CuentaDAO Post([FromBody] CuentaDAO cuentaUsuario)
        {
            Console.WriteLine("Contraseña: " + cuentaUsuario.contraseña);

            Cuenta cuenta= new Cuenta();
        
            cuenta.nombreUsuario= cuentaUsuario.nombreUsuario;
            cuenta.id= cuentaUsuario.id;
            cuenta.correoElectronico= cuentaUsuario.correoElectronico;
            cuenta.contraseña= cuentaUsuario.contraseña;
            cuenta.tipo= cuentaUsuario.tipo;

            try
            {
                byte[] bytesDeImagen = Convert.FromBase64String(cuentaUsuario.imagen);

                Image imagenAGuardar = (Bitmap)((new ImageConverter()).ConvertFrom(bytesDeImagen));
                imagenAGuardar.Save("C:/Users/BETO/Documents/" + cuentaUsuario.nombreUsuario + ".jpg", ImageFormat.Jpeg);

                context.Cuenta.Add(cuenta);
                context.SaveChanges();
                Console.WriteLine("Registro usuario");

                return cuentaUsuario;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.GetBaseException());

                return cuentaUsuario;
            }
        }


        [HttpPost("registroArtista")]
        public Artista PostArtista([FromBody] ArtistaDAO artistaDAO)
        {
            Artista artista = new Artista();
            artista.id = artistaDAO.id;
            artista.nombreArtistico = artistaDAO.nombreArtistico;
            artista.descripcion = artistaDAO.descripcion;

            try
            {
                byte[] bytesDeImagen = Convert.FromBase64String(artistaDAO.imagen);

                Image imagenAGuardar = (Bitmap)((new ImageConverter()).ConvertFrom(bytesDeImagen));
                imagenAGuardar.Save("C:/Users/BETO/Documents/" + artistaDAO.nombreArtistico + ".jpg", ImageFormat.Jpeg);

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
        public AlbumDAO PostAlbum([FromBody] AlbumDAO albumDao)
        {
            Album album = new Album();
            album.id= albumDao.id;
            album.nombre= albumDao.nombre;
            album.fecha= albumDao.fecha;
            album.descripcion= albumDao.descripcion;
            try
            {
                byte[] bytesDeImagen = Convert.FromBase64String(albumDao.imagen);

                Image imagenAGuardar = (Bitmap)((new ImageConverter()).ConvertFrom(bytesDeImagen));
                imagenAGuardar.Save("C:/Users/BETO/Documents/" + albumDao.nombre + ".jpg", ImageFormat.Jpeg);

                context.Album.Add(album);
                context.SaveChanges();

                return albumDao;
            }
            catch (Exception)
            {
                return albumDao;
            }
        }


       [HttpPost("registroCancion")]

       public Cancion PostCancion([FromBody] CancionSubida cancionSubida)
       {
            Cancion cancion = new Cancion();
            cancion.id = cancionSubida.id;
            cancion.nombre = cancionSubida.nombre;
            cancion.genero = cancionSubida.genero;
            cancion.duracion = cancionSubida.duracion;
            
            try
            {
                Console.WriteLine("Entró al try");
                byte[] bytes = System.Convert.FromBase64String(cancionSubida.audio);
                System.IO.File.WriteAllBytes("C:/Users/BETO/Documents/" + cancionSubida.nombre + ".mp3",bytes);
                context.Cancion.Add(cancion);
                context.SaveChanges();

                return cancion;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                
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
