using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ApiRestCuenta.DBContext;
using ApiRestCuenta.Entidad;
using Microsoft.AspNetCore.Mvc;

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
        [HttpPost]
        public ActionResult Post([FromBody]Cuenta cuenta)
        {
            try
            {
                context.Cuenta.Add(cuenta);
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
            }
        }


        [HttpPost("registroArtista")]
         public ActionResult PostArtista([FromBody]Artista artista)
        {
            try
            {
                
                context.Artista.Add(artista);
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
            }
        }


        [HttpPost("registroAlbum")]
        public ActionResult PostAlbum([FromBody]Album album)
        {
            try
            {
                
                context.Album.Add(album);
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
            }
        }


        [HttpPost("registroCancion")]
        public ActionResult PostCancion([FromBody]Cancion cancion)
        {
            try
            {
                
                context.Cancion.Add(cancion);
                context.SaveChanges();

                return Ok();
            }
            catch (Exception)
            {
                return BadRequest();
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
