{% if sticker.hasSong() %}
{% set song = sticker.getSong() %}
<div id="trax-player-container-{{ sticker.getId() }}" class="habbo-trax-player-widget"></div>
<script type="text/javascript">
(function() {
    var container = document.getElementById("trax-player-container-{{ sticker.getId() }}");
    if (!container || !window.HabboTraxPlayer) {
        return;
    }

    if (container._habboTraxPlayer) {
        container._habboTraxPlayer.stop();
        container.innerHTML = "";
    }

    container._habboTraxPlayer = new window.HabboTraxPlayer(container, {
        assetsPath: "{{ site.staticContentPath }}/habbo-widgets/traxplayer/",
        songUrl: "{{ site.sitePath }}/trax/song/{{ song.getId() }}",
        sampleUrl: "http://cdn.classichabbo.com/r38/dcr/hof_furni/mp3/",
        debug: false,
        allowSampleFallback: false
    });
}());
</script>
{% else %}
<img src="{{ site.staticContentPath }}/web-gallery/images/traxplayer/player.png"/>
{% endif %}
