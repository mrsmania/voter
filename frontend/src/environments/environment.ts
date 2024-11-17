class Environment {
  production = false;
  url = 'http://localhost';
  backendUrl = `${this.url}:8080`; // dont forget to add the port!
  apiUrl = `${this.backendUrl}/api`;
}

export const environment = new Environment();

