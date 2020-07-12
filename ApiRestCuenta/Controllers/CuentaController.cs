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

        [HttpGet("listaCanciones")]
        public IEnumerable<Cancion> getCanciones(string nombre){
            
            Console.WriteLine("Entro al get canciones");
            List<Cancion> listaCanciones = new List<Cancion>();
            var cancion=(from p in context.Cancion where p.nombre==nombre select p).FirstOrDefault();
            
            if(cancion!=null){
                listaCanciones.Add(new Cancion(cancion.id,cancion.nombre, cancion.genero,cancion.duracion, cancion.Album_id));
                var canciones = from s in context.Cancion where cancion.genero == s.genero select s;
                foreach(var valor in canciones){
                    if(valor.nombre!=nombre){
                        listaCanciones.Add(new Cancion(valor.id,valor.nombre,valor.genero,valor.duracion, valor.Album_id));
                    }
                }   
            }
            
            return listaCanciones;
        }

        [HttpGet("reproducirAudio")]
        public Audio obtenerCancion(string nombreCancion){
        Audio audio = new Audio();
        byte[] cancion;
        byte[] buffer =null;
        int longitud;
        var PathfileName = string.Empty;

        PathfileName = "/home/javier/Documentos/BasesDeDatos/Cancion/"+nombreCancion+".mp3";

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
        public IEnumerable<ArtistaSet> GetArtistas(){
            Console.WriteLine("Entro al get artistas");
            return context.ArtistaSet.ToList();
        }

        [HttpGet("listaDeCanciones")]
        public IEnumerable<Cancion> getCanciones(){
            return context.Cancion.ToList();
        }

        [HttpGet("obtenerImagenAlbum")]
        public Imagen getImagenAlbum(string nombreCancion){

            Console.WriteLine("Recuperando imagen");

        var cancion = context.Cancion.Where(x => x.nombre == nombreCancion).Select(x=>x.Album_id).FirstOrDefault();
        var album=context.Album.Where(X=> X.id == cancion).Select(x=>x.nombre).FirstOrDefault();
        byte[] imagen;
        byte[] buffer =null;
        int longitud;
        var PathfileName = string.Empty;

        PathfileName = "/home/javier/Documentos/BasesDeDatos/Album/"+album+".jpg";

        using (var fs = new FileStream(PathfileName, FileMode.Open, FileAccess.Read))
        {
            buffer = new byte[fs.Length];
            fs.Read(buffer, 0, (int)fs.Length);
            longitud = (int)fs.Length;
        }

        imagen=buffer;
        string vuelta = Convert.ToBase64String(imagen);
        Imagen imagen1 = new Imagen();
        imagen1.imagen=vuelta;

        return imagen1;

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
                System.IO.File.WriteAllBytes("/home/javier/Documentos/BasesDeDatos/Cuenta/" + cuentaUsuario.nombreUsuario + ".jpg",bytesDeImagen);

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
        public ArtistaSet PostArtista([FromBody] ArtistaDAO artistaDAO)
        {
            ArtistaSet artista = new ArtistaSet();
            artista.NombreArtistico = artistaDAO.nombreArtistico;
            artista.Descripcion = artistaDAO.descripcion;

            try
            {
                byte[] bytesDeImagen = Convert.FromBase64String(artistaDAO.imagen);
                System.IO.File.WriteAllBytes("/home/javier/Documentos/BasesDeDatos/Artista/" + artistaDAO.nombreArtistico + ".jpg",bytesDeImagen);
                context.ArtistaSet.Add(artista);
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
            Console.WriteLine("El id del artista es: " + albumDao.idArtista);

            Album album = new Album();
            album.id= albumDao.id;
            album.nombre= albumDao.nombre;
            album.fecha= albumDao.fecha;
            album.descripcion= albumDao.descripcion;

            var artista = (from per in context.ArtistaSet where per.id == albumDao.idArtista select per).First();

            album.ArtistaId = artista.id;

            try
            {
                byte[] bytesDeImagen = Convert.FromBase64String(albumDao.imagen);
                System.IO.File.WriteAllBytes("/home/javier/Documentos/BasesDeDatos/Album/" + albumDao.nombre + ".jpg",bytesDeImagen);

                context.Album.Add(album);
                context.SaveChanges();

                Console.WriteLine("registro Album");

                return albumDao;
            }
            catch (Exception)
            {
                return albumDao;
            }
        }


       [HttpPost("registroCancion")]

       public CancionSubida PostCancion([FromBody] CancionSubida cancionSubida)
       {
            Cancion cancion = new Cancion();
            cancion.id = cancionSubida.id;
            cancion.nombre = cancionSubida.nombre;
            cancion.genero = cancionSubida.genero;
            cancion.duracion = cancionSubida.duracion;
            cancion.Album_id = cancionSubida.idAlbum;
            
            try
            {
                Console.WriteLine("Entró al try");
                byte[] bytesDeImagen = Convert.FromBase64String(cancionSubida.audio);
                System.IO.File.WriteAllBytes("/home/javier/Documentos/BasesDeDatos/Cancion/" +cancionSubida.nombre + ".mp3",bytesDeImagen);               context.Cancion.Add(cancion);
                context.SaveChanges();

                Console.WriteLine("registro cancion");

                return cancionSubida;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                
                return cancionSubida;
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

         [HttpPost("registroListaDeReproduccion")]
       public ListaDeReproduccionSet PostListaDeReproduccion([FromBody] ListaDeReproduccionDAO listaDAO)
       {
           Console.WriteLine("Entro al servidor");
           Console.WriteLine(listaDAO.Cuenta_id);
           ListaDeReproduccionSet lista = new ListaDeReproduccionSet();
           lista.Nombre = listaDAO.Nombre;
           lista.Cuenta_id = listaDAO.Cuenta_id;
           
           try
            {
                context.ListaDeReproduccionSet.Add(lista);
                context.SaveChanges();

                return lista;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                
                return lista;
            }
        }

         [HttpGet("CancionesDeListaReproduccion")]
        public IEnumerable<string> GetcancionesDeLista(string nombreDeLista)
        {
            Console.WriteLine("Entro a obtner canciones");
            
            List<string> listaCanciones = new List<string>();
            
            try{
                var idLista = context.ListaDeReproduccionSet.Where(x => x.Nombre == nombreDeLista).Select(x => x.Id).FirstOrDefault();
                var idCanciones = context.CancionListaDeReproduccion.Where(x => x.ListaDeReproduccion_Id == idLista).Select(x => x.cancion_id).ToList();

                foreach(int idCancion in idCanciones){

                var nombreDeCancion = context.Cancion.Where(x => x.id == idCancion).Select(x => x.nombre).FirstOrDefault();

                listaCanciones.Add(nombreDeCancion);
            }

            return listaCanciones;

            }catch(Exception ){

                return listaCanciones;
            }
        }


         [HttpGet("ObtenerListasDeReproduccion")]
        public IEnumerable<string> GetListasDeReproduccion(int idCuenta)
        {
            List<string> litsav = new List<string>();
            try{
                var listas = context.ListaDeReproduccionSet.Where(x => x.Cuenta_id == idCuenta).Select(x=>x.Nombre).ToList();
                return listas;

            }catch(Exception e){

                Console.WriteLine("Entro al catch: " + e.Message);
                return litsav;
            }
        }

        [HttpPost("ligarCancionConLista")]
       public bool PostLigarLiastaConCancion(string nombreLista, string nombreCancion)
       {
           try
           {
               var idLista=context.ListaDeReproduccionSet.Where(x=>x.Nombre==nombreLista).Select(x=>x.Id).FirstOrDefault();
               var idCancion=context.Cancion.Where(x=>x.nombre==nombreCancion).Select(x=>x.id).FirstOrDefault();
               cancionListaDeReproduccion ligar = new cancionListaDeReproduccion();

               ligar.cancion_id = idCancion;
               ligar.ListaDeReproduccion_Id = idLista;

                context.CancionListaDeReproduccion.Add(ligar);
                context.SaveChanges();
                Console.WriteLine("Se realizó");
                return true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                
                return false;
            }
        }
    }
}
