use std::net::SocketAddr;
use std::path::Path;
use std::sync::Arc;
use std::sync::atomic::{AtomicU64, Ordering};
use std::time::Duration;

use axum::{Extension, http::StatusCode, Json, response::IntoResponse, Router, routing::get};
use axum_server::tls_rustls::RustlsConfig;
use reqwest::Client;
use serde_json::json;
use serde_json::Value;

struct DemoContext {
    client: Client,
    counter: AtomicU64,
}

#[tokio::main]
async fn main() {
    tracing_subscriber::fmt::init();

    let client = reqwest::Client::builder()
        .pool_idle_timeout(Duration::from_secs(55))
        .tcp_keepalive(Duration::from_secs(55))
        .build()
        .unwrap();

    let demo_context = Arc::new(DemoContext {
        client,
        counter: AtomicU64::new(1)
    });

    let app = Router::new()
        .route("/api/demo", get(demo_request))
        .layer(Extension(Arc::new(demo_context)));

    let tls_config = RustlsConfig::from_pem_file(
        Path::new("/home/demo/certs/fullchain.pem"),
        Path::new("/home/demo/certs/privatekey.pem"),
    ).await.unwrap();

    let addr = SocketAddr::from(([0, 0, 0, 0], 8080));
    tracing::debug!("listening on {}", addr);
    axum_server::bind_rustls(addr, tls_config)
    //axum_server::bind(addr)
        .serve(app.into_make_service())
        .await
        .unwrap();
}

async fn demo_request(Extension(demo_context): Extension<Arc<Arc<DemoContext>>>) -> impl IntoResponse {
    let client = &demo_context.client;
    let counter = &demo_context.counter;
    let id = counter.fetch_add(1, Ordering::SeqCst);
    let _ = post_request(&client, id, "http://host3:8080/api/request-rate-throttle-check").await;
    let _ = post_request(&client, id, "http://host3:8080/api/track").await;
    let _ = post_request(&client, id, "http://host3:8080/api/authenticate").await;
    let _ = post_request(&client, id, "http://host3:8080/api/authorize").await;
    let _ = get_request(&client, "http://host3:8080/api/products").await;
    let _ = get_request(&client, &format!("http://host3:8080/api/products/{}", id)).await;
    let _ = get_request(&client, &format!("http://host3:8080/api/products/{}/ratings", id)).await;
    let body = get_request(&client, &format!("http://host3:8080/api/products/{}/reviews", id)).await;
    (StatusCode::OK, Json(body))
}

async fn post_request(client: &Client, request_id: u64, uri: &str) -> Value {
    let request_json = json!({
        "requestId": request_id
    }).to_string();

    client.post(uri)
        .header("Content-Type", "application/json")
        .body(request_json)
        .send().await.unwrap()
        .json().await.unwrap()
}

async fn get_request(client: &Client, uri: &str) -> Value {
    client.get(uri)
        .send().await.unwrap()
        .json().await.unwrap()
}
