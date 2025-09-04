FROM nginx:latest

WORKDIR /etc/nginx

COPY nginx/nginx.conf /etc/nginx/nginx.conf

COPY nginx/nginx/conf.d /etc/nginx/conf.d

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]