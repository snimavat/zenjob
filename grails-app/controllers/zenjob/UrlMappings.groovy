package zenjob

class UrlMappings {

    static mappings = {

        get "/users/$id/preferences(.$format)?"(controller: "user", action:"preferences")
        get "/users/$id/recommendations(.$format)?"(controller: "user", action:"recommendations")
        post "/books/$id/preferences(.$format)?"(controller: "book", action:"createPreference")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
