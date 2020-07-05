using System.Threading;
using System.Threading.Tasks;
using Google.Protobuf.WellKnownTypes;
using Grpc.Core;
using NAudio.Wave;

namespace GrpcAudioStreaming.Server.Services
{
    public class AudioStreamService : AudioStream.AudioStreamBase
    {
        private IServerStreamWriter<AudioSample> _responseStream;
        AudioSampleSource audioSampleSource;


        public AudioStreamService()
        {
        }

        public override Task ObtenerStreamDeCancion(Cancion request, IServerStreamWriter<AudioSample> responseStream, ServerCallContext context)
        {
            audioSampleSource = new AudioSampleSource(@"wav\" + request.Nombre);
            _responseStream = responseStream;
            audioSampleSource.AudioSampleCreated += async (_, audioSample) => await _responseStream.WriteAsync(audioSample);
            return audioSampleSource.StartStreaming();
        }

        public override Task<AudioFormat> ElegirCancion(Cancion request, ServerCallContext context)
        {
            audioSampleSource = new AudioSampleSource();
            var nombre = request.Nombre;
            audioSampleSource.ObtenerAudioFormatDeCancion(@"wav\"+nombre);
            return Task.FromResult(audioSampleSource.AudioFormat); //le pasaba audioSampleSource.AudioFormat
        }
    }
}